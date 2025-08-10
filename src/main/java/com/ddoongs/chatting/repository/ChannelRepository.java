package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.ChannelTitleProjection;
import com.ddoongs.chatting.entity.ChannelEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {

  Optional<ChannelTitleProjection> findTitleByChannelId(@NonNull Long channelId);
}
