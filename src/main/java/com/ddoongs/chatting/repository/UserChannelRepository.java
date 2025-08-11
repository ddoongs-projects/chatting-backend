package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.dto.projection.ChannelProjection;
import com.ddoongs.chatting.dto.projection.UserIdProjection;
import com.ddoongs.chatting.entity.UserChannelEntity;
import com.ddoongs.chatting.entity.UserChannelId;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface UserChannelRepository extends JpaRepository<UserChannelEntity, UserChannelId> {

  boolean existsByUserIdAndChannelId(@NonNull Long userId, @NonNull Long channelId);

  List<UserIdProjection> findUserIdByChannelId(@NonNull Long channelId);

  @Query("SELECT c.channelId AS channelId, c.title as title, c.headCount as headCount "
         + "FROM UserChannelEntity uc "
         + "INNER JOIN ChannelEntity c ON uc.channelId = c.channelId "
         + "WHERE uc.userId = :userId")
  List<ChannelProjection> findChannelsByUserId(@NonNull @Param("userId") Long userId);

  void deleteByUserIdAndChannelId(@NonNull Long userId, @NonNull Long channelId);
}
