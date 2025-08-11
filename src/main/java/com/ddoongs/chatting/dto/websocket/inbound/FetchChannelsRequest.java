package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;

public class FetchChannelsRequest extends BaseRequest {


  public FetchChannelsRequest() {
    super(MessageType.FETCH_CHANNELS_REQUEST);
  }

}
