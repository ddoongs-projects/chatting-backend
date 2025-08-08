package com.ddoongs.chatting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "chat")
public class ChatEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chat_sequence")
  private Long chatSequence;

  @Column(name = "user_name", nullable = false)
  private String username;

  @Column(name = "content", nullable = false)
  private String content;


  protected ChatEntity() {
  }

  public ChatEntity(String username, String content) {
    this.username = username;
    this.content = content;
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
        chatSequence, username, content, this.getCreatedAt(), this.getUpdatedAt());
  }
}
