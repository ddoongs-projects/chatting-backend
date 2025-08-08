package com.ddoongs.chatting.service;

import java.time.Instant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

  private final SessionRepository<? extends Session> sessionRepository;

  public SessionService(SessionRepository<? extends Session> sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  public void refreshTTL(String httpSessionId) {
    Session session = sessionRepository.findById(httpSessionId);
    if (session != null) {
      session.setLastAccessedTime(Instant.now());
    }
  }

  public String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }
}
