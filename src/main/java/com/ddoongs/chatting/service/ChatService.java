package com.ddoongs.chatting.service;

import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.entity.ChatEntity;
import com.ddoongs.chatting.repository.ChatRepository;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

  private static final Logger log = LoggerFactory.getLogger(ChatService.class);

  private final ChannelService channelService;
  private final ChatRepository chatRepository;

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

    List<UserId> participantIds = channelService.getParticipantIds(channelId);
    participantIds.stream().filter(userId -> !userId.equals(senderUserId))
        .forEach(participantId -> {
          if (channelService.isOnline(participantId, channelId)) {
            messageSender.accept(participantId);
          }
        });
  }
}
