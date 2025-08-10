package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.AcceptInviteRequest;
import com.ddoongs.chatting.dto.websocket.outbound.AcceptInviteNotification;
import com.ddoongs.chatting.dto.websocket.outbound.AcceptInviteResponse;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.Optional;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class AcceptInviteRequestHandler implements BaseRequestHandler<AcceptInviteRequest> {

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public AcceptInviteRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, AcceptInviteRequest request) {
    UserId acceptorUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    Pair<Optional<UserId>, String> result = userConnectionService.accept(acceptorUserId,
        request.getUsername());

    result.getFirst().ifPresentOrElse(inviterUserId -> {
      webSocketSessionManager.sendMessage(
          senderSession,
          new AcceptInviteResponse(request.getUsername()));

      String acceptorUsername = result.getSecond();

      webSocketSessionManager.sendMessage(
          webSocketSessionManager.getSession(inviterUserId),
          new AcceptInviteNotification(acceptorUsername));
    }, () -> {
      String errorMessage = result.getSecond();
      webSocketSessionManager.sendMessage(
          senderSession,
          new ErrorResponse(MessageType.ACCEPT_INVITE_REQUEST, errorMessage));
    });
  }
}
