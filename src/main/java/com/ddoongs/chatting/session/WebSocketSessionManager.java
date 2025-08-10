package com.ddoongs.chatting.session;

import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.outbound.BaseMessage;
import com.ddoongs.chatting.json.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {

  private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);

  private final JsonUtils jsonUtils;

  private final Map<UserId, WebSocketSession> sessions = new ConcurrentHashMap<>();

  public WebSocketSessionManager(JsonUtils jsonUtils) {
    this.jsonUtils = jsonUtils;
  }

  public List<WebSocketSession> getSessions() {
    return sessions.values().stream().toList();
  }

  public WebSocketSession getSession(UserId userId) {
    return sessions.get(userId);
  }

  public void putSession(UserId userId, WebSocketSession session) {
    log.info("Store Session: {}", session.getId());
    sessions.put(userId, session);
  }

  public void closeSession(UserId userId) {
    try {
      WebSocketSession session = sessions.remove(userId);
      if (session != null) {
        log.info("Remove Session: {}", userId);
        session.close();
        log.info("Close Session: {}", userId);
      }
    } catch (IOException e) {
      log.error("Failed WebSocketSession Close. userId: {}", userId);
    }
  }

  public void sendMessage(WebSocketSession session, BaseMessage message) {
    jsonUtils.toJson(message).ifPresent(body -> {
      try {
        session.sendMessage(new TextMessage(body)); // 스레드 세이프 하지 않음
        log.info("Send Chat: [{}] to {}", body, session.getId());
      } catch (Exception ex) {
        log.error("메세지 전송 실패. error: {}", ex.getMessage());
      }
    });
  }
}
