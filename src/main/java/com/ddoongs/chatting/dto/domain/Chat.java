package com.ddoongs.chatting.security.dto.domain;

public record Chat(String username, String content) {

  public static Chat ofSystem(String content) {
    return new Chat("system", content);
  }
}
