package com.ddoongs.chatting.service;

import com.ddoongs.chatting.constants.IdKey;
import com.ddoongs.chatting.dto.domain.ChannelId;
import com.ddoongs.chatting.dto.domain.UserId;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

  private static final String NAMESPACE = "message:user";
  private static final long TTL = 300L;

  private static final Logger log = LoggerFactory.getLogger(SessionService.class);

  private final SessionRepository<? extends Session> sessionRepository;
  private final StringRedisTemplate stringRedisTemplate;

  public SessionService(SessionRepository<? extends Session> sessionRepository,
      StringRedisTemplate stringRedisTemplate) {
    this.sessionRepository = sessionRepository;
    this.stringRedisTemplate = stringRedisTemplate;
  }

  public void refreshTTL(UserId userId, String httpSessionId) {
    String channelIdKey = buildChannelIdKey(userId);
    try {

      Session session = sessionRepository.findById(httpSessionId);
      if (session != null) {
        session.setLastAccessedTime(Instant.now());
        stringRedisTemplate.expire(channelIdKey, TTL, TimeUnit.SECONDS);
      }
    } catch (Exception ex) {
      log.error("Redis set failed. key: {}", channelIdKey);
    }
  }

  public boolean setActiveChannel(UserId userId, ChannelId channelId) {
    String channelIdKey = buildChannelIdKey(userId);
    try {
      stringRedisTemplate.opsForValue()
          .set(channelIdKey, channelId.id().toString(), TTL, TimeUnit.SECONDS);
      return true;
    } catch (Exception ex) {
      log.error("Redis set failed. key: {}, channelId: {}", channelIdKey, channelId.id());
    }
  }

  public String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }

  private String buildChannelIdKey(UserId userId) {
    return "%s:%d:%s".formatted(NAMESPACE, userId.id(), IdKey.CHANNEL_ID);
  }
}
