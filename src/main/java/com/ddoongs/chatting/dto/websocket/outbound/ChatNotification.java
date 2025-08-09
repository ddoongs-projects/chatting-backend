package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;

public class ChatNotification extends BaseMessage {

  private final String username;
  private final String content;

  public ChatNotification(String username, String content) {
    super(MessageType.NOTIFY_CHAT);
    this.username = username;
    this.content = content;
  }

  public String getUsername() {
    return username;
  }

  public String getContent() {
    return content;
  }
}
