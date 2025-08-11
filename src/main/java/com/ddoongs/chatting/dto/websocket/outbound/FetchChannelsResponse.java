package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.Channel;
import java.util.List;

public class FetchChannelsResponse extends BaseMessage {

  private final List<Channel> channels;

  public FetchChannelsResponse(List<Channel> channels) {
    super(MessageType.FETCH_CHANNELS_RESPONSE);
    this.channels = channels;
  }

  public List<Channel> getChannels() {
    return channels;
  }
}
