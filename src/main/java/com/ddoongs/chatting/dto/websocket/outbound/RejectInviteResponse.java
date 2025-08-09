package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;

public class RejectInviteResponse extends BaseMessage {

  private final String username;
  private final UserConnectionsStatus status;

  public RejectInviteResponse(String username, UserConnectionsStatus status) {
    super(MessageType.REJECT_INVITE_RESPONSE);
    this.username = username;
    this.status = status;
  }

  public String getUsername() {
    return username;
  }

  public UserConnectionsStatus getStatus() {
    return status;
  }
}
