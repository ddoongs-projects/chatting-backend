package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.KeepAlive;
import com.ddoongs.chatting.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class KeepAliveHandler implements BaseRequestHandler<KeepAlive> {

  private final SessionService sessionService;

  public KeepAliveHandler(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, KeepAlive request) {
    UserId senderUserId = (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    sessionService.refreshTTL(senderUserId,
        (String) senderSession.getAttributes().get(IdKey.HTTP_SESSION_ID.getValue()));
  }
}
