package com.ddoongs.chatting.service;

import com.ddoongs.chatting.constants.ResultType;
import com.ddoongs.chatting.dto.domain.Channel;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.projection.ChannelTitleProjection;
import com.ddoongs.chatting.entity.ChannelEntity;
import com.ddoongs.chatting.entity.UserChannelEntity;
import com.ddoongs.chatting.repository.ChannelRepository;
import com.ddoongs.chatting.repository.UserChannelRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelService {

  private static final Logger log = LoggerFactory.getLogger(ChannelService.class);

  private final UserConnectionService userConnectionService;
  private final SessionService sessionService;
  private final ChannelRepository channelRepository;
  private final UserChannelRepository userChannelRepository;

  public ChannelService(UserConnectionService userConnectionService, SessionService sessionService,
      ChannelRepository channelRepository, UserChannelRepository userChannelRepository) {
    this.userConnectionService = userConnectionService;
    this.sessionService = sessionService;
    this.channelRepository = channelRepository;
    this.userChannelRepository = userChannelRepository;
  }

  public boolean isJoined(UserId userId, ChannelId channelId) {
    return userChannelRepository.existsByUserIdAndChannelId(userId.id(), channelId.id());
  }

  public List<UserId> getParticipantIds(ChannelId channelId) {
    return userChannelRepository.findUserIdByChannelId(channelId.id())
        .stream()
        .map(userId -> new UserId(userId.getUserId()))
        .toList();
  }

  public boolean isOnline(UserId userId, ChannelId channelId) {
    return sessionService.isOnline(userId, channelId);
  }

  @Transactional
  public Pair<Optional<Channel>, ResultType> create(UserId senderUserId, UserId participantId,
      String title) {
    if (title == null || title.isEmpty()) {
      log.warn("Invalid args: title is empty");
      return Pair.of(Optional.empty(), ResultType.INVALID_ARGS);
    }

    try {
      final int HEAD_COUNT = 2;
      ChannelEntity channelEntity = channelRepository.save(new ChannelEntity(title, HEAD_COUNT));
      Long channelId = channelEntity.getChannelId();
      List<UserChannelEntity> userChannelEntities = List.of(
          new UserChannelEntity(senderUserId.id(), channelId, 0L),
          new UserChannelEntity(participantId.id(), channelId, 0L)
      );
      userChannelRepository.saveAll(userChannelEntities);

      Channel channel = new Channel(new ChannelId(channelId), title, HEAD_COUNT);
      return Pair.of(Optional.of(channel), ResultType.SUCCESS);
    } catch (Exception ex) {
      log.error("Create failed. cause: {}", ex.getMessage());
      throw ex;
    }
  }

  public Pair<Optional<String>, ResultType> enter(UserId userId, ChannelId channelId) {
    if (!isJoined(userId, channelId)) {
      log.warn("Enter channel failed. User not joined the channel. userId: {}, channelId: {}",
          userId, channelId);
      return Pair.of(Optional.empty(), ResultType.NOT_JOINED);
    }

    Optional<String> title = channelRepository.findTitleByChannelId(channelId.id())
        .map(ChannelTitleProjection::getTitle);
    if (title.isEmpty()) {
      log.warn("Enter channel failed. Channel does not exist. userId: {}, channelId: {}",
          userId, channelId);
      return Pair.of(Optional.empty(), ResultType.NOT_FOUND);
    }

    if (sessionService.setActiveChannel(userId, channelId)) {
      return Pair.of(title, ResultType.SUCCESS);
    }

    log.error("Enter channel failed. userId: {}, channelId: {}", userId, channelId);
    return Pair.of(Optional.empty(), ResultType.FAILED);
  }
}
