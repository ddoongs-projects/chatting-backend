package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.LeaveChannelRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.LeaveChannelResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class LeaveChannelRequestHandler implements
    BaseRequestHandler<LeaveChannelRequest> {

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public LeaveChannelRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, LeaveChannelRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());

    if (channelService.leave(senderUserId)) {
      webSocketSessionManager.sendMessage(senderSession, new LeaveChannelResponse());
    } else {
      new ErrorResponse(MessageType.LEAVE_CHANNEL_REQUEST, "Leave failed.");
    }
  }
}
