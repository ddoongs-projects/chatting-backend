package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.CountProjection;
import com.ddoongs.chatting.dto.projection.InviteCodeProjection;
import com.ddoongs.chatting.dto.projection.UserIdProjection;
import com.ddoongs.chatting.dto.projection.UsernameProjection;
import com.ddoongs.chatting.entity.UserEntity;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(@NonNull String username);

  List<UserIdProjection> findUserIdsByUsernameIn(@NonNull Collection<String> usernames);

  Optional<UsernameProjection> findUsernameByUserId(@NonNull Long userId);

  Optional<UserEntity> findByInviteCode(@NonNull String inviteCode);

  Optional<InviteCodeProjection> findInviteCodeByUserId(@NonNull Long userId);

  Optional<CountProjection> findCountByUserId(@NonNull Long userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<UserEntity> findForUpdateByUserId(@NonNull Long userId);
}
