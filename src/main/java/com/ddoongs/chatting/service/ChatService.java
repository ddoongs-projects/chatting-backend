package com.ddoongs.chatting.service;

import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.entity.ChatEntity;
import com.ddoongs.chatting.repository.ChatRepository;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

  private static final int THREAD_POOL_SIZE = 10;

  private static final Logger log = LoggerFactory.getLogger(ChatService.class);

  private final ChannelService channelService;
  private final ChatRepository chatRepository;
  private final ExecutorService senderThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

  public ChatService(ChannelService channelService, ChatRepository chatRepository) {
    this.channelService = channelService;
    this.chatRepository = chatRepository;
  }

  public void sendMessage(UserId senderUserId, ChannelId channelId, String content,
      Consumer<UserId> messageSender) {
    try {
      chatRepository.save(new ChatEntity(senderUserId.id(), content));
    } catch (Exception ex) {
      log.error("Send message failed. cause: {}", ex.getMessage());
      return;
    }

    channelService.getOnlineParticipantIds(channelId)
        .stream().filter(participantId -> !participantId.equals(senderUserId))
        .forEach(
            participantId -> CompletableFuture.runAsync(() -> messageSender.accept(participantId)));
  }
}
