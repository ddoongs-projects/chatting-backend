package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WriteChat extends BaseRequest {

  private final ChannelId channelId;
  private final String content;

  @JsonCreator
  public WriteChat(
      @JsonProperty("channelId") ChannelId channelId,
      @JsonProperty("content") String content) {
    super(MessageType.WRITE_CHAT);
    this.channelId = channelId;
    this.content = content;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

  public String getContent() {
    return content;
  }
}
