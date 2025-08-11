package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.InviteCode;

public class FetchChannelInviteCodeResponse extends BaseMessage {

  private final ChannelId channelId;
  private final InviteCode inviteCode;


  public FetchChannelInviteCodeResponse(ChannelId channelId, InviteCode inviteCode) {
    super(MessageType.CREATE_CHANNEL_RESPONSE);
    this.channelId = channelId;
    this.inviteCode = inviteCode;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

  public InviteCode getInviteCode() {
    return inviteCode;
  }
}
