package com.ddoongs.chatting.dto;

public record Chat(String username, String content) {

  public static Chat ofSystem(String content) {
    return new Chat("system", content);
  }
}
