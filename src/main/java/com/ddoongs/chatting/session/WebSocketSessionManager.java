package com.ddoongs.chatting.session;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {

  private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);

  private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

  public List<WebSocketSession> getSessions() {
    return sessions.values().stream().toList();
  }

  public void storeSession(WebSocketSession session) {
    log.info("Store Session: {}", session.getId());
    sessions.put(session.getId(), session);
  }

  public void terminateSession(String sessionId) {
    try {
      WebSocketSession session = sessions.remove(sessionId);
      if (session != null) {
        log.info("Remove Session: {}", sessionId);
        session.close();
        log.info("Close Session: {}", sessionId);
      }
    } catch (IOException e) {
      log.error("Failed WebSocketSession Clode. sessionId: {}", sessionId);
    }
  }
}
