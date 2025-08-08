package com.ddoongs.chatting.entity;

import com.ddoongs.chatting.constants.UserFriendStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@IdClass(UserConnectionId.class)
@Table(name = "user_connection")
public class UserConnectionEntity extends BaseEntity {

  @Id
  @Column(name = "partner_a_user_id")
  private Long partnerAUserId;

  @Id
  @Column(name = "partner_b_user_id")
  private Long partnerBUserId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UserFriendStatus status;

  @Column(name = "invite_user_id", nullable = false)
  private Long inviteUserId;

  protected UserConnectionEntity() {
  }

  public UserConnectionEntity(Long partnerAUserId, Long partnerBUserId, UserFriendStatus status,
      Long inviteUserId) {
    this.partnerAUserId = partnerAUserId;
    this.partnerBUserId = partnerBUserId;
    this.status = status;
    this.inviteUserId = inviteUserId;
  }

  public Long getPartnerAUserId() {
    return partnerAUserId;
  }

  public Long getPartnerBUserId() {
    return partnerBUserId;
  }

  public UserFriendStatus getStatus() {
    return status;
  }

  public void setStatus(UserFriendStatus status) {
    this.status = status;
  }

  public Long getInviteUserId() {
    return inviteUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserConnectionEntity that = (UserConnectionEntity) o;
    return Objects.equals(partnerAUserId, that.partnerAUserId) && Objects.equals(
        partnerBUserId, that.partnerBUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partnerAUserId, partnerBUserId);
  }

  @Override
  public String toString() {
    return "UserConnectionEntity{partnerAUserId=%d, partnerBUserId=%d, status=%s, inviteUserId=%d}"
        .formatted(partnerAUserId, partnerBUserId, status, inviteUserId);
  }
}
