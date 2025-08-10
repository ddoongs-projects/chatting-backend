package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.entity.UserChannelEntity;
import com.ddoongs.chatting.entity.UserChannelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserChannelRepository extends JpaRepository<UserChannelEntity, UserChannelId> {

  boolean existsByUserIdAndChannelId(@NonNull Long userId, @NonNull Long channelId);
}
