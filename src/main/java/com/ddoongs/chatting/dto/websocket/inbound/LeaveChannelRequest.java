package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;

public class LeaveChannelRequest extends BaseRequest {

  public LeaveChannelRequest() {
    super(MessageType.LEAVE_CHANNEL_REQUEST);
  }

}
