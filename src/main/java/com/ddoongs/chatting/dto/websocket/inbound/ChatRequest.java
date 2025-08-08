package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest extends BaseRequest {

  private final String username;
  private final String content;

  @JsonCreator
  public ChatRequest(@JsonProperty("username") String username,
      @JsonProperty("content") String content) {
    super(MessageType.CHAT);
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
