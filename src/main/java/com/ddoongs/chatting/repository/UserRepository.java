package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.CountProjection;
import com.ddoongs.chatting.dto.projection.InviteCodeProjection;
import com.ddoongs.chatting.dto.projection.UsernameProjection;
import com.ddoongs.chatting.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(@NonNull String username);

  Optional<UsernameProjection> findUsernameByUserId(@NonNull Long userId);

  Optional<UserEntity> findByConnectionInviteCode(@NonNull String connectionInviteCode);

  Optional<InviteCodeProjection> findInviteCodeByUserId(@NonNull Long userId);

  Optional<CountProjection> findCountByUserId(@NonNull Long userId);

  //  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<UserEntity> findForUpdateByUserId(@NonNull Long userId);
}
