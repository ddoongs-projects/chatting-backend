package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.WriteChat;
import com.ddoongs.chatting.dto.websocket.outbound.ChatNotification;
import com.ddoongs.chatting.service.ChatService;
import com.ddoongs.chatting.service.UserService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WriteChatHandler implements BaseRequestHandler<WriteChat> {

  private final UserService userService;
  private final ChatService chatService;
  private final WebSocketSessionManager webSocketSessionManager;

  public WriteChatHandler(UserService userService, ChatService chatService,
      WebSocketSessionManager webSocketSessionManager) {
    this.userService = userService;
    this.chatService = chatService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, WriteChat request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    ChannelId channelId = request.getChannelId();
    String content = request.getContent();
    String senderUsername = userService.getUsername(senderUserId).orElse("unknown");

    Consumer<UserId> messageSender = (participantId) -> {
      WebSocketSession participantSession = webSocketSessionManager.getSession(participantId);
      if (participantSession != null) {
        webSocketSessionManager.sendMessage(
            participantSession,
            new ChatNotification(channelId, senderUsername, content)
        );
      }
    };

    chatService.sendMessage(senderUserId, channelId, content, messageSender);
  }
}
