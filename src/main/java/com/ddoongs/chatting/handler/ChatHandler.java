package com.ddoongs.chatting.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(ChatHandler.class);

  private final ObjectMapper objectMapper;

  private WebSocketSession leftSide;
  private WebSocketSession rightSide;

  public ChatHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("Connection Established: {}", session.getId());

    if (leftSide == null) {
      leftSide = session;
      return;
    } else if (rightSide == null) {
      rightSide = session;
      return;
    }

    log.warn("빈 자리 없음, {}의 접속 거부", session.getId());
    session.close();
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    log.error("Transport Error: [{}] from {}", exception.getMessage(), session.getId());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
    log.info("Connection Closed: [{}] from {}", status, session.getId());

    if (leftSide == session) {
      leftSide = null;
    } else if (rightSide == session) {
      rightSide = null;
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) {
    log.info("Received Text Message: [{}] from {}", message, session.getId());

    String payload = message.getPayload();
    try {

      Chat recievedChat = objectMapper.readValue(payload, Chat.class);
      if (leftSide == session) {
        sendMessage(rightSide, recievedChat.content());
      } else if (rightSide == session) {
        sendMessage(leftSide, recievedChat.content());
      }
    } catch (JsonProcessingException ex) {
      String errorMessage = "유효한 프로토콜이 아닙니다.";
      log.error("errorMessage payload: {} from {}", payload, session.getId());
      sendMessage(session, errorMessage);
    }
  }

  private void sendMessage(WebSocketSession session, String content) {
    try {
      String body = objectMapper.writeValueAsString(new Chat(content));
      session.sendMessage(new TextMessage(body));
      log.info("Send Chat: [{}] to {}", body, session.getId());
    } catch (IOException ex) {
      log.error("메세지 전송 실패 to {} error: {}", session.getId(), ex.getMessage());
    }
  }
}
