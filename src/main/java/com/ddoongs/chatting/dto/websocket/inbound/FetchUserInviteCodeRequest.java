package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;

public class FetchUserInviteCodeRequest extends BaseRequest {

  public FetchUserInviteCodeRequest() {
    super(MessageType.FETCH_USER_INVITE_CODE_REQUEST);
  }
}
