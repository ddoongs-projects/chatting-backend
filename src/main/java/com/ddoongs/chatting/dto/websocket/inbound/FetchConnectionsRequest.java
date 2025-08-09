package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchConnectionsRequest extends BaseRequest {

  private final UserConnectionsStatus status;

  public FetchConnectionsRequest(@JsonProperty("status") UserConnectionsStatus status) {
    super(MessageType.FETCH_CONNECTIONS_REQUEST);
    this.status = status;
  }

  public UserConnectionsStatus getStatus() {
    return status;
  }
}
