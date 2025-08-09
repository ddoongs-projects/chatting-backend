package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.InviteCode;

public class FetchUserInviteResponse extends BaseMessage {

  private final InviteCode inviteCode;

  public FetchUserInviteResponse(InviteCode inviteCode) {
    super(MessageType.FETCH_USER_INVITE_CODE_RESPONSE);
    this.inviteCode = inviteCode;
  }

  public InviteCode getInviteCode() {
    return inviteCode;
  }
}
