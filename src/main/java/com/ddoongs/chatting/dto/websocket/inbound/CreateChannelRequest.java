package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateChannelRequest extends BaseRequest {

  private final String title;
  private final String participantUsername;

  @JsonCreator
  public CreateChannelRequest(@JsonProperty("title") String title,
      @JsonProperty("participantUsername") String participantUsername) {
    super(MessageType.CREATE_CHANNEL_REQUEST);
    this.title = title;
    this.participantUsername = participantUsername;
  }

  public String getTitle() {
    return title;
  }

  public String getParticipantUsername() {
    return participantUsername;
  }
}
