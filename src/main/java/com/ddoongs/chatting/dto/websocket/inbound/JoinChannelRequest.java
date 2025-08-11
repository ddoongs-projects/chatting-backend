package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.InviteCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinChannelRequest extends BaseRequest {

  private final InviteCode inviteCode;

  public JoinChannelRequest(@JsonProperty("inviteCode") InviteCode inviteCode) {
    super(MessageType.JOIN_CHANNEL_REQUEST);
    this.inviteCode = inviteCode;
  }

  public InviteCode getInviteCode() {
    return inviteCode;
  }
}
