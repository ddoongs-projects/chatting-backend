package com.ddoongs.chatting.config;

import com.ddoongs.chatting.handler.ChatHandler;
import com.ddoongs.chatting.security.auth.WebSocketHttpSessionHandShakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

  private final ChatHandler chatHandler;
  private final WebSocketHttpSessionHandShakeInterceptor webSocketHttpSessionHandShakeInterceptor;

  public WebSocketConfig(ChatHandler chatHandler,
      WebSocketHttpSessionHandShakeInterceptor webSocketHttpSessionHandShakeInterceptor) {
    this.chatHandler = chatHandler;
    this.webSocketHttpSessionHandShakeInterceptor = webSocketHttpSessionHandShakeInterceptor;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(chatHandler, "/ws/v1/chat")
        .addInterceptors(webSocketHttpSessionHandShakeInterceptor);
  }

}
