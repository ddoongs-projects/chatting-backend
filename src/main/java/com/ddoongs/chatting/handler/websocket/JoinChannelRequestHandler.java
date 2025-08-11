package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.ResultType;
import com.ddoongs.chatting.dto.domain.Channel;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.JoinChannelRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.JoinChannelResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.Optional;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class JoinChannelRequestHandler implements BaseRequestHandler<JoinChannelRequest> {

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public JoinChannelRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, JoinChannelRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    Pair<Optional<Channel>, ResultType> result;

    try {
      result = channelService.join(senderUserId,
          request.getInviteCode());
    } catch (Exception ex) {
      webSocketSessionManager.sendMessage(
          senderSession,
          new ErrorResponse(MessageType.JOIN_CHANNEL_REQUEST, ResultType.FAILED.getMessage()));
      return;
    }

    result.getFirst().ifPresentOrElse(
        channel -> webSocketSessionManager.sendMessage(
            senderSession,
            new JoinChannelResponse(channel.channelId(), channel.title())),
        () -> webSocketSessionManager.sendMessage(
            senderSession,
            new ErrorResponse(MessageType.JOIN_CHANNEL_REQUEST, result.getSecond().getMessage())));
  }
}
