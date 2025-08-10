package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;

public class CreateChannelResponse extends BaseMessage {

  private final ChannelId channelId;
  private final String title;

  public CreateChannelResponse(ChannelId channelId, String title) {
    super(MessageType.CREATE_CHANNEL_RESPONSE);
    this.channelId = channelId;
    this.title = title;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

  public String getTitle() {
    return title;
  }
}
