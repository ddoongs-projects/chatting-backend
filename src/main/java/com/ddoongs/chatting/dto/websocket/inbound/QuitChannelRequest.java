package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuitChannelRequest extends BaseRequest {

  private final ChannelId channelId;

  public QuitChannelRequest(@JsonProperty("channelId") ChannelId channelId) {
    super(MessageType.ENTER_CHANNEL_REQUEST);
    this.channelId = channelId;
  }

  public ChannelId getChannelId() {
    return channelId;
  }
}
