package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;

public class AcceptInviteNotification extends BaseMessage {

  private final String username;

  public AcceptInviteNotification(String username) {
    super(MessageType.NOTIFY_ACCEPT);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
