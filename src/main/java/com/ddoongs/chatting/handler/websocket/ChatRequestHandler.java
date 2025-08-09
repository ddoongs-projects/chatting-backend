package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.dto.domain.Chat;
import com.ddoongs.chatting.dto.websocket.inbound.WriteChatRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ChatNotification;
import com.ddoongs.chatting.entity.ChatEntity;
import com.ddoongs.chatting.repository.ChatRepository;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatRequestHandler implements BaseRequestHandler<WriteChatRequest> {

  private final WebSocketSessionManager webSocketSessionManager;
  private final ChatRepository chatRepository;

  public ChatRequestHandler(WebSocketSessionManager webSocketSessionManager,
      ChatRepository chatRepository) {
    this.webSocketSessionManager = webSocketSessionManager;
    this.chatRepository = chatRepository;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, WriteChatRequest request) {
    Chat receivedChat = new Chat(request.getUsername(), request.getContent());
    chatRepository.save(new ChatEntity(receivedChat.username(), receivedChat.content()));

    webSocketSessionManager.getSessions().stream()
        .filter(session -> !session.getId().equals(senderSession.getId()))
        .forEach(participantSession -> webSocketSessionManager.sendMessage(participantSession,
            new ChatNotification(receivedChat.username(), receivedChat.content())));
  }
}
