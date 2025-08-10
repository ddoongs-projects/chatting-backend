package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;

public class KeepAlive extends BaseRequest {

  public KeepAlive() {
    super(MessageType.KEEP_ALIVE);
  }
}
