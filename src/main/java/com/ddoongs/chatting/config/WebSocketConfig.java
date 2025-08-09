package com.ddoongs.chatting.config;

import com.ddoongs.chatting.auth.WebSocketHttpSessionHandShakeInterceptor;
import com.ddoongs.chatting.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

  private final WebSocketHandler webSocketHandler;
  private final WebSocketHttpSessionHandShakeInterceptor webSocketHttpSessionHandShakeInterceptor;

  public WebSocketConfig(WebSocketHandler webSocketHandler,
      WebSocketHttpSessionHandShakeInterceptor webSocketHttpSessionHandShakeInterceptor) {
    this.webSocketHandler = webSocketHandler;
    this.webSocketHttpSessionHandShakeInterceptor = webSocketHttpSessionHandShakeInterceptor;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler, "/ws/v1/chat")
        .addInterceptors(webSocketHttpSessionHandShakeInterceptor);
  }

}
