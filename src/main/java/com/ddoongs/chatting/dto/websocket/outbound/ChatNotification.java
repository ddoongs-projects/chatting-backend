package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.ChannelId;

public class ChatNotification extends BaseMessage {

  private final ChannelId channelId;
  private final String username;
  private final String content;

  public ChatNotification(

      ChannelId channelId,
      String username, String content) {
    super(MessageType.NOTIFY_CHAT);
    this.channelId = channelId;
    this.username = username;
    this.content = content;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

  public String getUsername() {
    return username;
  }

  public String getContent() {
    return content;
  }
}
