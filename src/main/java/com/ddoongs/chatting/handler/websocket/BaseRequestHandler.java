package com.ddoongs.chatting.handler.websocket;

import com.ddoongs.chatting.dto.websocket.inbound.BaseRequest;
import org.springframework.web.socket.WebSocketSession;

public interface BaseRequestHandler<T extends BaseRequest> {

  void handleRequest(WebSocketSession webSocketSession, T request);

}
