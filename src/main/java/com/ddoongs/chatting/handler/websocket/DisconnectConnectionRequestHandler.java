package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.DisconnectConnectionRequest;
import com.ddoongs.chatting.dto.websocket.outbound.DisconnectConnectionResponse;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class DisconnectConnectionRequestHandler implements
    BaseRequestHandler<DisconnectConnectionRequest> {

  private static final Logger log = LoggerFactory.getLogger(
      DisconnectConnectionRequestHandler.class);

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public DisconnectConnectionRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, DisconnectConnectionRequest request) {
    UserId senderUserId = (UserId) senderSession.getAttributes().get(Constants.USER_ID.getValue());
    Pair<Boolean, String> result =
        userConnectionService.disconnect(senderUserId, request.getUsername());
    if (result.getFirst()) {
      webSocketSessionManager.sendMessage(senderSession,
          new DisconnectConnectionResponse(request.getUsername(),
              UserConnectionsStatus.DISCONNECTED));
    } else {
      String errorMessage = result.getSecond();
      webSocketSessionManager.sendMessage(senderSession,
          new ErrorResponse(MessageType.DISCONNECT_CONNECTION_REQUEST, errorMessage));
    }
  }
}
