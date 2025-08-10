package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnterChannelRequest extends BaseRequest {

  private final ChannelId channelId;

  public EnterChannelRequest(@JsonProperty("channelId") ChannelId channelId) {
    super(MessageType.ENTER_CHANNEL_REQUEST);
    this.channelId = channelId;
  }

  public ChannelId getChannelId() {
    return channelId;
  }
}
