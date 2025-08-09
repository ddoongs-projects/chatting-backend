package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RejectInviteRequest extends BaseRequest {

  private final String username;

  @JsonCreator
  public RejectInviteRequest(@JsonProperty("username") String username) {
    super(MessageType.REJECT_INVITE_REQUEST);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
