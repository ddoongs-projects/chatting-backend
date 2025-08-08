package com.ddoongs.chatting.security.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
public class ChatUserDetails implements UserDetails {

  private final Long userId;
  private final String username;
  private String password;

  public ChatUserDetails(
      @JsonProperty("userId") Long userId,
      @JsonProperty("username") String username,
      @JsonProperty("password") String password) {
    this.userId = userId;
    this.username = username;
    this.password = password;
  }

  public Long getUserId() {
    return userId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatUserDetails that = (ChatUserDetails) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }

  @Override
  public String toString() {
    return "ChatUserDetails{userId=%d, username='%s', password='%s'}"
        .formatted(userId, username, password);
  }

  public void erasePassword() {
    password = "";
  }
}
