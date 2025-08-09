package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.InviteCode;

public class InviteResponse extends BaseMessage {

  private final InviteCode inviteCode;
  private final UserConnectionsStatus status;

  public InviteResponse(InviteCode inviteCode, UserConnectionsStatus status) {
    super(MessageType.INVITE_RESPONSE);
    this.inviteCode = inviteCode;
    this.status = status;
  }

  public InviteCode getInviteCode() {
    return inviteCode;
  }

  public UserConnectionsStatus getStatus() {
    return status;
  }
}
