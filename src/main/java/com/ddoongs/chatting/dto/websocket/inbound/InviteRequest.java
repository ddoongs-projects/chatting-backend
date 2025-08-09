package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.InviteCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteRequest extends BaseRequest {

  private final InviteCode userInviteCode;

  @JsonCreator
  public InviteRequest(@JsonProperty("userInviteCode") InviteCode userInviteCode) {
    super(MessageType.INVITE_REQUEST);
    this.userInviteCode = userInviteCode;
  }

  public InviteCode getUserInviteCode() {
    return userInviteCode;
  }
}
