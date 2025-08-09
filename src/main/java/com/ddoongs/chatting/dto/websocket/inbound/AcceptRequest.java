package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AcceptRequest extends BaseRequest {

  private final String username;

  @JsonCreator
  public AcceptRequest(@JsonProperty("username") String username) {
    super(MessageType.ACCEPT_REQUEST);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
