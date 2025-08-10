package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;

public class KeepAliveRequest extends BaseRequest {

  public KeepAliveRequest() {
    super(MessageType.KEEP_ALIVE);
  }
}
