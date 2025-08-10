package com.ddoongs.chatting.handler;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.BaseRequest;
import com.ddoongs.chatting.handler.websocket.RequestDispatcher;
import com.ddoongs.chatting.json.JsonUtils;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

  private final JsonUtils jsonUtils;
  private final WebSocketSessionManager webSocketSessionManager;
  private final RequestDispatcher requestDispatcher;

  public WebSocketHandler(JsonUtils jsonUtils, WebSocketSessionManager webSocketSessionManager,
      RequestDispatcher requestDispatcher) {
    this.jsonUtils = jsonUtils;
    this.webSocketSessionManager = webSocketSessionManager;
    this.requestDispatcher = requestDispatcher;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.info("Connection Established: {}", session.getId());

    ConcurrentWebSocketSessionDecorator concurrentWebSocketSessionDecorator =
        new ConcurrentWebSocketSessionDecorator(session, 5000, 100 * 1024);
    UserId userId = (UserId) session.getAttributes().get(IdKey.USER_ID.getValue());
    webSocketSessionManager.putSession(userId, concurrentWebSocketSessionDecorator);

    // 하나의 WebSocketSession 객체(클라이언트 A의 세션)에 대해 두 개의 다른 스레드가 동시에 sendMessage() 메소드를 호출하게 됩니다.
    // sessionManager.storeSession(session);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    log.error("Transport Error: [{}] from {}", exception.getMessage(), session.getId());
    UserId userId = (UserId) session.getAttributes().get(IdKey.USER_ID.getValue());
    webSocketSessionManager.closeSession(userId);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
    log.info("Connection Closed: [{}] from {}", status, session.getId());
    UserId userId = (UserId) session.getAttributes().get(IdKey.USER_ID.getValue());
    webSocketSessionManager.closeSession(userId);
  }

  @Override
  protected void handleTextMessage(WebSocketSession senderSession, @NonNull TextMessage message) {
    log.info("Received Text Message: [{}] from {}", message.getPayload(), senderSession.getId());

    String payload = message.getPayload();
    jsonUtils.fromJson(payload, BaseRequest.class)
        .ifPresent(request -> requestDispatcher.dispatchRequest(senderSession, request));

  }

}
