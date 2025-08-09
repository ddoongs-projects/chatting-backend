package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.AcceptRequest;
import com.ddoongs.chatting.dto.websocket.outbound.AcceptNotification;
import com.ddoongs.chatting.dto.websocket.outbound.AcceptResponse;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.Optional;
import org.springframework.data.util.Pair;
import org.springframework.web.socket.WebSocketSession;

public class AcceptRequestHandler implements BaseRequestHandler<AcceptRequest> {

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public AcceptRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, AcceptRequest request) {
    UserId acceptorUserId =
        (UserId) senderSession.getAttributes().get(Constants.USER_ID.getValue());
    Pair<Optional<UserId>, String> result = userConnectionService.accept(acceptorUserId,
        request.getUsername());

    result.getFirst().ifPresentOrElse(inviterUserId -> {
      webSocketSessionManager.sendMessage(
          senderSession,
          new AcceptResponse(request.getUsername()));

      String acceptorUsername = result.getSecond();

      webSocketSessionManager.sendMessage(
          webSocketSessionManager.getSession(inviterUserId),
          new AcceptNotification(acceptorUsername));
    }, () -> {
      String errorMessage = result.getSecond();
      webSocketSessionManager.sendMessage(
          senderSession,
          new ErrorResponse(MessageType.ACCEPT_REQUEST, errorMessage));
    });
  }
}
