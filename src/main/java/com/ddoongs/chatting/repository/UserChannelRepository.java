package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.UserIdProjection;
import com.ddoongs.chatting.entity.UserChannelEntity;
import com.ddoongs.chatting.entity.UserChannelId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserChannelRepository extends JpaRepository<UserChannelEntity, UserChannelId> {

  boolean existsByUserIdAndChannelId(@NonNull Long userId, @NonNull Long channelId);

  List<UserIdProjection> findUserIdByChannelId(@NonNull Long channelId);

}
