package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DisconnectConnectionRequest extends BaseRequest {

  private final String username;

  @JsonCreator
  public DisconnectConnectionRequest(@JsonProperty("username") String username) {
    super(MessageType.DISCONNECT_CONNECTION_REQUEST);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
