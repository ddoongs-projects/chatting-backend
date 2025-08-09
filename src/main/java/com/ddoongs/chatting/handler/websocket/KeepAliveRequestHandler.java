package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.dto.websocket.inbound.KeepAliveRequest;
import com.ddoongs.chatting.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class KeepAliveRequestHandler implements BaseRequestHandler<KeepAliveRequest> {

  private final SessionService sessionService;

  public KeepAliveRequestHandler(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, KeepAliveRequest request) {
    sessionService.refreshTTL(
        (String) senderSession.getAttributes().get(Constants.HTTP_SESSION_ID.getValue()));
  }
}
