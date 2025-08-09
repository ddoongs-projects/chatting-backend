package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;

public class AcceptNotification extends BaseMessage {

  private final String username;

  public AcceptNotification(String username) {
    super(MessageType.NOTIFY_ACCEPT);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
