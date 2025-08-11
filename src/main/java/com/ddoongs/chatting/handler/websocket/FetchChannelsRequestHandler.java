package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.Channel;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.websocket.inbound.FetchChannelsRequest;
import com.ddoongs.chatting.dto.websocket.outbound.FetchChannelsResponse;
import com.ddoongs.chatting.service.ChannelService;
import com.ddoongs.chatting.session.WebSocketSessionManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class FetchChannelsRequestHandler implements
    BaseRequestHandler<FetchChannelsRequest> {

  private static final Logger log = LoggerFactory.getLogger(
      FetchChannelsRequestHandler.class);

  private final ChannelService channelService;
  private final WebSocketSessionManager webSocketSessionManager;

  public FetchChannelsRequestHandler(ChannelService channelService,
      WebSocketSessionManager webSocketSessionManager) {
    this.channelService = channelService;
    this.webSocketSessionManager = webSocketSessionManager;
  }

  @Override
  public void handleRequest(WebSocketSession senderSession, FetchChannelsRequest request) {
    UserId senderUserId = (UserId) senderSession.getAttributes().get(IdKey.USER_ID.getValue());
    List<Channel> channels = channelService.getChannels(senderUserId);

    webSocketSessionManager.sendMessage(senderSession, new FetchChannelsResponse(channels));
  }
}
