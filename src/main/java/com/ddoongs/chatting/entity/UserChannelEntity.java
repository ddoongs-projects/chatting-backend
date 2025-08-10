package com.ddoongs.chatting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_channel")
@IdClass(UserChannelId.class)
public class UserChannelEntity extends BaseEntity {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @Id
  @Column(name = "channel_id")
  private Long channelId;

  @Column(name = "last_read_message_seq", nullable = false)
  private Long lastReadMessageSeq;

  protected UserChannelEntity() {
  }

  public UserChannelEntity(Long userId, Long channelId, Long lastReadMessageSeq) {
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadMessageSeq = lastReadMessageSeq;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserChannelEntity that = (UserChannelEntity) o;
    return Objects.equals(userId, that.userId) && Objects.equals(channelId, that.channelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, channelId);
  }

  @Override
  public String toString() {
    return "UserChannelEntity{userId=%d, channelId=%d, lastReadMessageSeq=%d, createdAt=%s, updatedAt=%s}"
        .formatted(userId, channelId, lastReadMessageSeq, this.getCreatedAt(), this.getUpdatedAt());
  }

  public Long getUserId() {
    return userId;
  }

  public Long getChannelId() {
    return channelId;
  }

  public Long getLastReadMessageSeq() {
    return lastReadMessageSeq;
  }

  public void setLastReadMessageSeq(Long lastReadMessageSeq) {
    this.lastReadMessageSeq = lastReadMessageSeq;
  }
}