package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.InviteCode;

public class FetchUserInviteCodeResponse extends BaseMessage {

  private final InviteCode inviteCode;

  public FetchUserInviteCodeResponse(InviteCode inviteCode) {
    super(MessageType.FETCH_USER_INVITE_CODE_RESPONSE);
    this.inviteCode = inviteCode;
  }

  public InviteCode getInviteCode() {
    return inviteCode;
  }
}
