package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;

public class AcceptInviteResponse extends BaseMessage {

  private final String username;

  public AcceptInviteResponse(String username) {
    super(MessageType.ACCEPT_INVITE_RESPONSE);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
