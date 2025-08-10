package com.ddoongs.chatting.auth;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.UserId;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class WebSocketHttpSessionHandShakeInterceptor extends HttpSessionHandshakeInterceptor {


  private static final Logger log =
      LoggerFactory.getLogger(WebSocketHttpSessionHandShakeInterceptor.class);

  @Override
  public boolean beforeHandshake(
      @NonNull ServerHttpRequest request,
      @NonNull ServerHttpResponse response,
      @NonNull WebSocketHandler wsHandler,
      @NonNull Map<String, Object> attributes
  ) {
    if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null) {
        log.warn("WebSocket handshake failed. authentication is null");
        return false;
      }

      HttpSession httpSession = servletServerHttpRequest.getServletRequest().getSession(false);
      if (httpSession == null) {
        log.info("WebSocket handshake failed. httpSession is null");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
      }

      ChatUserDetails chatUserDetails = (ChatUserDetails) authentication.getPrincipal();
      attributes.put(IdKey.HTTP_SESSION_ID.getValue(), httpSession.getId());
      attributes.put(IdKey.USER_ID.getValue(), new UserId(chatUserDetails.getUserId()));
      return true;
    }

    log.info("WebSocket handshake failed. request: {}", request.getClass());
    response.setStatusCode(HttpStatus.BAD_REQUEST);
    return false;
  }
}
