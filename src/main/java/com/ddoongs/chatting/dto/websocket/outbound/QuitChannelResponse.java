package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;

public class QuitChannelResponse extends BaseMessage {

  private final ChannelId channelId;

  public QuitChannelResponse(ChannelId channelId) {
    super(MessageType.QUIT_CHANNEL_RESPONSE);
    this.channelId = channelId;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

}
