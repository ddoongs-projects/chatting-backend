package com.ddoongs.chatting.handler;

import com.ddoongs.chatting.constants.Constants;
import com.ddoongs.chatting.dto.domain.Chat;
import com.ddoongs.chatting.dto.websocket.inbound.BaseRequest;
import com.ddoongs.chatting.dto.websocket.inbound.ChatRequest;
import com.ddoongs.chatting.dto.websocket.inbound.KeepAliveRequest;
import com.ddoongs.chatting.entity.ChatEntity;
import com.ddoongs.chatting.repository.ChatRepository;
import com.ddoongs.chatting.service.SessionService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
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
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(ChatHandler.class);

  private final ObjectMapper objectMapper;
  private final WebSocketSessionManager webSocketSessionManager;
  private final ChatRepository chatRepository;
  private final SessionService sessionService;

  public ChatHandler(ObjectMapper objectMapper, WebSocketSessionManager webSocketSessionManager,
      ChatRepository chatRepository, SessionService sessionService) {
    this.objectMapper = objectMapper;
    this.webSocketSessionManager = webSocketSessionManager;
    this.chatRepository = chatRepository;
    this.sessionService = sessionService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.info("Connection Established: {}", session.getId());

    ConcurrentWebSocketSessionDecorator concurrentWebSocketSessionDecorator =
        new ConcurrentWebSocketSessionDecorator(session, 5000, 100 * 1024);

    webSocketSessionManager.storeSession(concurrentWebSocketSessionDecorator);

    // 하나의 WebSocketSession 객체(클라이언트 A의 세션)에 대해 두 개의 다른 스레드가 동시에 sendMessage() 메소드를 호출하게 됩니다.
    // sessionManager.storeSession(session);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    log.error("Transport Error: [{}] from {}", exception.getMessage(), session.getId());
    webSocketSessionManager.terminateSession(session.getId());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
    log.info("Connection Closed: [{}] from {}", status, session.getId());
    webSocketSessionManager.terminateSession(session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession senderSession, @NonNull TextMessage message) {
    log.info("Received Text Message: [{}] from {}", message.getPayload(), senderSession.getId());

    String payload = message.getPayload();
    try {
      BaseRequest baseRequest = objectMapper.readValue(payload, BaseRequest.class);

      if (baseRequest instanceof ChatRequest chatRequest) {
        Chat receivedChat = new Chat(chatRequest.getUsername(), chatRequest.getContent());
        chatRepository.save(new ChatEntity(receivedChat.username(), receivedChat.content()));

        webSocketSessionManager.getSessions().stream()
            .filter(session -> !session.getId().equals(senderSession.getId()))
            .forEach(participantSession -> sendMessage(participantSession, receivedChat));
      } else if (baseRequest instanceof KeepAliveRequest) {
        sessionService.refreshTTL(
            (String) senderSession.getAttributes().get(Constants.HTTP_SESSION_ID.getValue()));
      }

    } catch (JsonProcessingException ex) {
      String errorMessage = "유효한 프로토콜이 아닙니다.";
      log.error("errorMessage payload: {} from {}", payload, senderSession.getId());
      sendMessage(senderSession, Chat.ofSystem(errorMessage));
    }
  }

  private void sendMessage(WebSocketSession session, Chat chat) {
    try {
      String body = objectMapper.writeValueAsString(chat);
      session.sendMessage(new TextMessage(body)); // 스레드 세이프 하지 않음
      log.info("Send Chat: [{}] to {}", body, session.getId());
    } catch (IOException ex) {
      log.error("메세지 전송 실패 to {} error: {}", session.getId(), ex.getMessage());
    }
  }
}
