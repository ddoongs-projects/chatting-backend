package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.InviteRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.InviteNotification;
import com.ddoongs.chatting.dto.websocket.outbound.InviteResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.web.socket.WebSocketSession;

public class InviteRequestHandler implements BaseRequestHandler<InviteRequest> {

  private static final Logger log = LoggerFactory.getLogger(InviteRequestHandler.class);

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public InviteRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, InviteRequest request) {
    UserId inviterUserId = (UserId) senderSession.getAttributes().get(Constants.USER_ID.getValue());
    Pair<Optional<UserId>, String> result = userConnectionService.invite(inviterUserId,
        request.getUserInviteCode());

    result.getFirst().ifPresentOrElse(partnerUserId -> {
      String inviterUsername = result.getSecond();
      webSocketSessionManager.sendMessage(
          senderSession,
          new InviteResponse(request.getUserInviteCode(), UserConnectionsStatus.PENDING));
      webSocketSessionManager.sendMessage(
          webSocketSessionManager.getSession(partnerUserId),
          new InviteNotification(inviterUsername));
    }, () -> {
      String errorMessage = result.getSecond();
      webSocketSessionManager.sendMessage(
          senderSession,
          new ErrorResponse(MessageType.INVITE_REQUEST, errorMessage));
    });
  }
}
