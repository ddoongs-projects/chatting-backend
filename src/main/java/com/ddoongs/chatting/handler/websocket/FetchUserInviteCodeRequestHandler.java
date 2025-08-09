package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.FetchUserInviteCodeRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.dto.websocket.outbound.FetchUserInviteResponse;
import com.ddoongs.chatting.service.UserService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class FetchUserInviteCodeRequestHandler implements
    BaseRequestHandler<FetchUserInviteCodeRequest> {

  private static final Logger log = LoggerFactory.getLogger(
      FetchUserInviteCodeRequestHandler.class);

  private final UserService userService;
  private final WebSocketSessionManager webSocketSessionManager;

  public FetchUserInviteCodeRequestHandler(UserService userService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userService = userService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, FetchUserInviteCodeRequest request) {
    UserId senderUserId = (UserId) senderSession.getAttributes().get(Constants.USER_ID.getValue());
    userService.getInviteCode(senderUserId).ifPresentOrElse((inviteCode) -> {
      webSocketSessionManager.sendMessage(senderSession, new FetchUserInviteResponse(inviteCode));
    }, () -> {
      webSocketSessionManager.sendMessage(senderSession, new ErrorResponse(
          MessageType.FETCH_USER_INVITE_CODE_REQUEST, "Fetch user invite code failed"));
    });
  }
}
