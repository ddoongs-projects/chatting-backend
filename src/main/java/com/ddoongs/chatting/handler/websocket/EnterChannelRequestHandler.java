package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.constants.ResultType;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.EnterChannelRequest;
import com.ddoongs.chatting.dto.websocket.outbound.EnterChannelResponse;
import com.ddoongs.chatting.dto.websocket.outbound.ErrorResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.Optional;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class EnterChannelRequestHandler implements
    BaseRequestHandler<EnterChannelRequest> {

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public EnterChannelRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, EnterChannelRequest request) {
    UserId senderUserId =
        (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());

    Pair<Optional<String>, ResultType> result = channelService.enter(senderUserId,
        request.getChannelId());

    result.getFirst().ifPresentOrElse(
        title -> webSocketSessionManager.sendMessage(
            senderSession,
            new EnterChannelResponse(request.getChannelId(), title)),
        () -> webSocketSessionManager.sendMessage(senderSession,
            new ErrorResponse(MessageType.ENTER_CHANNEL_REQUEST, result.getSecond()
                .getMessage()))
    );
  }
}
