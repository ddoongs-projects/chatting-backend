package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.ResultType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.QuitChannelRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.QuitChannelResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class QuitChannelRequestHandler implements
    BaseRequestHandler<QuitChannelRequest> {

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public QuitChannelRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, QuitChannelRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());

    ResultType result;
    try {
      result = channelService.quit(senderUserId,
          request.getChannelId());
    } catch (Exception ex) {
      new ErrorResponse(MessageType.QUIT_CHANNEL_REQUEST, ResultType.FAILED.getMessage());
      return;
    }

    if (result == ResultType.SUCCESS) {
      webSocketSessionManager.sendMessage(
          senderSession,
          new QuitChannelResponse(request.getChannelId()));
    } else {
      new ErrorResponse(MessageType.QUIT_CHANNEL_REQUEST, result.getMessage());
    }

  }
}
