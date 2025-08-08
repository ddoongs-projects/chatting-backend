package com.ddoongs.chatting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "chat_user")
public class UserEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "connection_invite_code", nullable = false)
  private String connectionInviteCode;

  @Column(name = "connection_count", nullable = false)
  private int connectionCount;

  protected UserEntity() {
  }

  public UserEntity(String username, String password) {
    this.username = username;
    this.password = password;
    this.connectionInviteCode = UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserEntity that = (UserEntity) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }

  @Override
  public String toString() {
    return "ChatUserEntity{userId=%d, username='%s', createdAt=%s, updatedAt=%s}"
        .formatted(userId, username, this.getCreatedAt(), this.getUpdatedAt());
  }

  public Long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getConnectionInviteCode() {
    return connectionInviteCode;
  }

  public int getConnectionCount() {
    return connectionCount;
  }

  public void setConnectionCount(int friendCount) {
    this.connectionCount = friendCount;
  }
}
