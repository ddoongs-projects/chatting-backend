package com.ddoongs.chatting.dto.domain;

public record ChannelId(Long id) {

  public ChannelId {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Invalid channel id");
    }
  }
}
