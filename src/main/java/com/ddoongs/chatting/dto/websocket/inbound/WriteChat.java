package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WriteChat extends BaseRequest {

  private final String username;
  private final String content;

  @JsonCreator
  public WriteChat(@JsonProperty("username") String username,
      @JsonProperty("content") String content) {
    super(MessageType.WRITE_CHAT);
    this.username = username;
    this.content = content;
  }

  public String getUsername() {
    return username;
  }

  public String getContent() {
    return content;
  }
}
