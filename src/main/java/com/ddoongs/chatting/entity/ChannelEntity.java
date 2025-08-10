package com.ddoongs.chatting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "channel")
public class ChannelEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "channel_id")
  private Long channelId;

  @Column(name = "title", nullable = false, length = 20)
  private String title;

  @Column(name = "channel_invite_code", nullable = false, length = 32, unique = true)
  private String channelInviteCode;

  @Column(name = "head_count", nullable = false)
  private int headCount;

  protected ChannelEntity() {
  }

  public ChannelEntity(String title, int headCount) {
    this.title = title;
    this.headCount = headCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelEntity that = (ChannelEntity) o;
    return Objects.equals(channelId, that.channelId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(channelId);
  }

  @Override
  public String toString() {
    return "ChannelEntity{channelId=%d, title='%s', channelInviteCode='%s', headCount=%d, createdAt=%s, updatedAt=%s}"
        .formatted(channelId, title, channelInviteCode, headCount, this.getCreatedAt(),
            this.getUpdatedAt());
  }

  public Long getChannelId() {
    return channelId;
  }

  public String getTitle() {
    return title;
  }

  public String getChannelInviteCode() {
    return channelInviteCode;
  }

  public int getHeadCount() {
    return headCount;
  }

  public void setHeadCount(int headCount) {
    this.headCount = headCount;
  }
}