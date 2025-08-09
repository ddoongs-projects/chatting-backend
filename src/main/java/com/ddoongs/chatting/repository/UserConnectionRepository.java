package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.projection.InviterUserIdProjection;
import com.ddoongs.chatting.dto.projection.UserConnectionStatusProjection;
import com.ddoongs.chatting.entity.UserConnectionEntity;
import com.ddoongs.chatting.entity.UserConnectionId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
