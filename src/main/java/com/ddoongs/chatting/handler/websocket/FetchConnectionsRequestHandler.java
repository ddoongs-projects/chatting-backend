package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.Connection;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.FetchConnectionsRequest;
import com.ddoongs.chatting.dto.websocket.outbound.FetchConnectionsResponse;
import com.ddoongs.chatting.service.UserConnectionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class FetchConnectionsRequestHandler implements
    BaseRequestHandler<FetchConnectionsRequest> {

  private static final Logger log = LoggerFactory.getLogger(
      FetchConnectionsRequestHandler.class);

  private final UserConnectionService userConnectionService;
  private final WebSocketSessionManager webSocketSessionManager;

  public FetchConnectionsRequestHandler(UserConnectionService userConnectionService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userConnectionService = userConnectionService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, FetchConnectionsRequest request) {
    UserId senderUserId = (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    List<Connection> connections = userConnectionService.getUsersByStatus(senderUserId,
            request.getStatus()).stream()
        .map(user -> new Connection(user.username(), request.getStatus()))
        .toList();

    webSocketSessionManager.sendMessage(senderSession, new FetchConnectionsResponse(connections));
  }
}
