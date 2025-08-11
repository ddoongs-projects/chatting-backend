package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.ResultType;
import com.ddoongs.chatting.dto.domain.Channel;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.CreateChannelRequest;
import com.ddoongs.chatting.dto.websocket.outbound.ChannelJoinNotification;
import com.ddoongs.chatting.dto.websocket.outbound.CreateChannelResponse;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.service.UserService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class CreateChannelRequestHandler implements BaseRequestHandler<CreateChannelRequest> {

  private final ChannelService channelService;
  private final UserService userService;
  private final WebSocketSessionManager webSocketSessionManager;

  public CreateChannelRequestHandler(ChannelService channelService, UserService userService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.userService = userService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, CreateChannelRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());

    List<UserId> participantUserIds = userService.getUserIds(request.getParticipantUsernames());

    if (participantUserIds.isEmpty()) {
      webSocketSessionManager.sendMessage(senderSession,
          new ErrorResponse(MessageType.CREATE_CHANNEL_REQUEST,
              ResultType.NOT_FOUND.getMessage()));
      return;
    }

    Pair<Optional<Channel>, ResultType> result;
    try {
      result = channelService.create(senderUserId, participantUserIds, request.getTitle());
    } catch (Exception ex) {
      webSocketSessionManager.sendMessage(senderSession,
          new ErrorResponse(MessageType.CREATE_CHANNEL_REQUEST,
              ResultType.FAILED.getMessage()));
      return;
    }

    if (result.getFirst().isEmpty()) {
      webSocketSessionManager.sendMessage(senderSession,
          new ErrorResponse(MessageType.CREATE_CHANNEL_REQUEST,
              result.getSecond().getMessage()));
      return;
    }

    Channel channel = result.getFirst().get();

    webSocketSessionManager.sendMessage(
        senderSession,
        new CreateChannelResponse(channel.channelId(),
            channel.title()));

    participantUserIds.forEach(participantUserId ->
        CompletableFuture.runAsync(() ->
            {
              WebSocketSession participantSession =
                  webSocketSessionManager.getSession(participantUserId);
              if (participantSession != null) {
                webSocketSessionManager.sendMessage(
                    participantSession,
                    new ChannelJoinNotification(channel.channelId(), channel.title()));
              }
            }
        ));
  }
}
