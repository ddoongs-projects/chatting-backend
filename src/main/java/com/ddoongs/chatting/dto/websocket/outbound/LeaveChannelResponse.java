package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;

public class LeaveChannelResponse extends BaseMessage {

  public LeaveChannelResponse() {
    super(MessageType.LEAVE_CHANNEL_RESPONSE);
  }

}
