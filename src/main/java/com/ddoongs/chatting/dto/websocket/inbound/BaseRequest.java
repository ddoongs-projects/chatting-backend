package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FetchUserInviteCodeRequest.class, name = MessageType.FETCH_USER_INVITE_CODE_REQUEST),
    @JsonSubTypes.Type(value = FetchConnectionsRequest.class, name = MessageType.FETCH_CONNECTIONS_REQUEST),
    @JsonSubTypes.Type(value = InviteRequest.class, name = MessageType.INVITE_REQUEST),
    @JsonSubTypes.Type(value = AcceptInviteRequest.class, name = MessageType.ACCEPT_INVITE_REQUEST),
    @JsonSubTypes.Type(value = RejectInviteRequest.class, name = MessageType.REJECT_INVITE_REQUEST),
    @JsonSubTypes.Type(value = WriteChatRequest.class, name = MessageType.WRITE_CHAT),
    @JsonSubTypes.Type(value = KeepAliveRequest.class, name = MessageType.KEEP_ALIVE),
})
public class BaseRequest {

  private final String type;

  public BaseRequest(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
