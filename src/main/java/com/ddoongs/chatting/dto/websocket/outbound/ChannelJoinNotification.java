package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;

public class ChannelJoinNotification extends BaseMessage {

  private final ChannelId channelId;
  private final String title;

  public ChannelJoinNotification(ChannelId channelId, String title) {
    super(MessageType.NOTIFY_CHANNEL_JOIN);
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
