package com.ddoongs.chatting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "chat")
public class ChatEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chat_sequence", nullable = false)
  private Long chatSequence;

  @Column(name = "user_name", nullable = false)
  private String username;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected ChatEntity() {
  }

  public ChatEntity(String username, String content) {
    this.username = username;
    this.content = content;
  }

  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatEntity that = (ChatEntity) o;
    return Objects.equals(chatSequence, that.chatSequence);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(chatSequence);
  }

  @Override
  public String toString() {
    return "ChatEntity{chatSequence=%d, username='%s', content='%s', createdAt=%s, updatedAt=%s}".formatted(
        chatSequence, username, content, createdAt, updatedAt);
  }
}
