package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.projection.InviterUserIdProjection;
import com.ddoongs.chatting.dto.projection.UserConnectionStatusProjection;
import com.ddoongs.chatting.dto.projection.UserIdUsernameInviterUserIdProjection;
import com.ddoongs.chatting.entity.UserConnectionEntity;
import com.ddoongs.chatting.entity.UserConnectionId;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface UserConnectionRepository extends
    JpaRepository<UserConnectionEntity, UserConnectionId> {

  Optional<UserConnectionStatusProjection> findStatusByPartnerAUserIdAndPartnerBUserId(
      @NonNull Long partnerAUserId, @NonNull Long partnerBUserId);

  Optional<UserConnectionEntity> findByPartnerAUserIdAndPartnerBUserIdAndStatus(
      Long partnerAUserId,
      @NonNull Long partnerBUserId,
      @NonNull UserConnectionsStatus status);

  Optional<InviterUserIdProjection> findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
      @NonNull Long partnerAUserId, @NonNull Long partnerBUserId);

  long countByPartnerAUserIdAndPartnerBUserIdInAndStatus(
      @NonNull Long partnerAUserId,
      @NonNull Collection<Long> partnerBUserIds,
      @NonNull UserConnectionsStatus status);

  long countByPartnerBUserIdAndPartnerAUserIdInAndStatus(
      @NonNull Long partnerBUserId,
      @NonNull Collection<Long> partnerAUserIds,
      @NonNull UserConnectionsStatus status);

  @Query(
      "SELECT u.partnerBUserId AS userId, userB.username AS username, u.inviterUserId AS inviterUserId "
      + "FROM UserConnectionEntity u "
      + "INNER JOIN UserEntity userB "
      + "ON u.partnerBUserId = userB.userId "
      + "WHERE u.partnerAUserId = :userId AND u.status = :status"
  )
  List<UserIdUsernameInviterUserIdProjection> findConnectionsByPartnerAUserIdAndStatus(
      @Param("userId") Long userId,
      @Param("status") UserConnectionsStatus status);

  @Query(
      "SELECT u.partnerAUserId AS userId, userA.username AS username, u.inviterUserId AS inviterUserId  "
      + "FROM UserConnectionEntity u "
      + "INNER JOIN UserEntity userA "
      + "ON u.partnerAUserId = userA.userId "
      + "WHERE u.partnerBUserId = :userId AND u.status = :status"
  )
  List<UserIdUsernameInviterUserIdProjection> findConnectionsByPartnerBUserIdAndStatus(
      @Param("userId") Long userId,
      @Param("status") UserConnectionsStatus status);
}
