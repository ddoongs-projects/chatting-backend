package com.ddoongs.chatting.dto.websocket.inbound;

import com.ddoongs.chatting.constants.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CreateChannelRequest extends BaseRequest {

  private final String title;
  private final List<String> participantUsernames;

  @JsonCreator
  public CreateChannelRequest(@JsonProperty("title") String title,
      @JsonProperty("participantUsernames") List<String> participantUsernames) {
    super(MessageType.CREATE_CHANNEL_REQUEST);
    this.title = title;
    this.participantUsernames = participantUsernames;
  }

  public String getTitle() {
    return title;
  }

  public List<String> getParticipantUsernames() {
    return participantUsernames;
  }
}
