package com.ddoongs.chatting.dto.websocket.outbound;

public abstract class BaseMessage {

  private final String type;

  public BaseMessage(String type) {
    this.type = type;
  }
}
