package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.FetchChannelInviteCodeRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.FetchChannelInviteCodeResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class FetchChannelInviteCodeRequestHandler implements
    BaseRequestHandler<FetchChannelInviteCodeRequest> {

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public FetchChannelInviteCodeRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, FetchChannelInviteCodeRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());

    if (!channelService.isJoined(senderUserId, request.getChannelId())) {
      webSocketSessionManager.sendMessage(senderSession, new ErrorResponse(
          MessageType.FETCH_CHANNEL_INVITE_CODE_REQUEST, "Not joined the channel"));

      channelService.getInviteCode(request.getChannelId()).ifPresentOrElse(
          inviteCode -> webSocketSessionManager.sendMessage(senderSession,
              new FetchChannelInviteCodeResponse(
                  request.getChannelId(),
                  inviteCode
              )), () -> {
            webSocketSessionManager.sendMessage(senderSession, new ErrorResponse(
                MessageType.FETCH_CHANNEL_INVITE_CODE_REQUEST,
                "Fetch channel invite code failed."));
          }
      );
    }

  }
}
