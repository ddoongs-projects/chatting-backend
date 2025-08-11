package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.ChannelProjection;
import com.ddoongs.chatting.dto.projection.ChannelTitleProjection;
import com.ddoongs.chatting.dto.projection.InviteCodeProjection;
import com.ddoongs.chatting.entity.ChannelEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {

  Optional<ChannelTitleProjection> findTitleByChannelId(@NonNull Long channelId);

  Optional<InviteCodeProjection> findInviteCodeByChannelId(@NonNull Long channelId);

  Optional<ChannelProjection> findChannelCodeByInviteCode(@NonNull String inviteCode);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ChannelEntity> findForUpdateByChannelId(@NonNull Long channelId);

}
