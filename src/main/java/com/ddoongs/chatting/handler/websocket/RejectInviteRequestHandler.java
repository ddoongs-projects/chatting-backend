package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.RejectInviteRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.RejectInviteResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class RejectInviteRequestHandler implements BaseRequestHandler<RejectInviteRequest> {

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public RejectInviteRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, RejectInviteRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(Constants.USER_ID.getValue());
    Pair<Boolean, String> result = userConnectionService.reject(senderUserId,
        request.getUsername());

    if (result.getFirst()) {
      webSocketSessionManager.sendMessage(senderSession,
          new RejectInviteResponse(request.getUsername(),
              UserConnectionsStatus.REJECTED));
    } else {
      String errorMessage = result.getSecond();
      webSocketSessionManager.sendMessage(
          senderSession,
          new ErrorResponse(MessageType.REJECT_INVITE_REQUEST, errorMessage)
      );
    }
  }
}
