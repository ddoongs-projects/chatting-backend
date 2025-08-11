package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchChannelInviteCodeRequest extends BaseRequest {

  private final ChannelId channelId;

  public FetchChannelInviteCodeRequest(@JsonProperty("channelId") ChannelId channelId) {
    super(MessageType.FETCH_CHANNEL_INVITE_CODE_REQUEST);
    this.channelId = channelId;
  }

  public ChannelId getChannelId() {
    return channelId;
  }
}
