package com.ddoongs.chatting.service;

import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.entity.UserConnectionEntity;
import com.ddoongs.chatting.entity.UserEntity;
import com.ddoongs.chatting.repository.UserConnectionRepository;
import com.ddoongs.chatting.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserConnectionLimitService {

  private final UserRepository userRepository;
  private final UserConnectionRepository userConnectionRepository;

  private int limitConnections = 1_000;

  public UserConnectionLimitService(UserRepository userRepository,
      UserConnectionRepository userConnectionRepository) {
    this.userRepository = userRepository;
    this.userConnectionRepository = userConnectionRepository;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public void setLimitConnections(int limitConnections) {
    this.limitConnections = limitConnections;
  }

  @Transactional
  public void accept(UserId acceptorUserId, UserId inviterUserId) {
    Long firstUserId = Long.min(acceptorUserId.id(), inviterUserId.id());
    Long secondUserId = Long.max(acceptorUserId.id(), inviterUserId.id());

    // 정렬해야 lock 상황 발생하지 않음
    UserEntity firstUserEntity = userRepository.findForUpdateByUserId(firstUserId)
        .orElseThrow(() -> new EntityNotFoundException("Invalid userId: " + firstUserId));

    UserEntity secondUserEntity = userRepository.findForUpdateByUserId(secondUserId)
        .orElseThrow(() -> new EntityNotFoundException("Invalid userId: " + secondUserId));

    UserConnectionEntity userConnectionEntity = userConnectionRepository
        .findByPartnerAUserIdAndPartnerBUserIdAndStatus(
            firstUserId,
            secondUserId, UserConnectionsStatus.PENDING)
        .orElseThrow(() -> new EntityNotFoundException("Invalid status."));

    Function<Long, String> getErrorMessage = userId -> userId.equals(acceptorUserId.id())
        ? "Connection limit reached"
        : "Connection limit reached by other user";

    int firstConnectionCount = firstUserEntity.getConnectionCount();
    if (firstConnectionCount >= limitConnections) {
      throw new IllegalStateException(getErrorMessage.apply(firstUserId));
    }

    int secondConnectionCount = secondUserEntity.getConnectionCount();
    if (secondConnectionCount >= limitConnections) {
      throw new IllegalStateException(getErrorMessage.apply(secondUserId));
    }

    firstUserEntity.setConnectionCount(firstConnectionCount + 1);
    secondUserEntity.setConnectionCount(secondConnectionCount + 1);
    userConnectionEntity.setStatus(UserConnectionsStatus.ACCEPTED);
  }
}
