package com.ddoongs.chatting.service;

import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.InviteCode;
import com.ddoongs.chatting.dto.domain.User;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.entity.UserConnectionEntity;
import com.ddoongs.chatting.repository.UserConnectionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserConnectionService {

  private static final Logger log = LoggerFactory.getLogger(UserConnectionService.class);

  private final UserService userService;
  private final UserConnectionRepository userConnectionRepository;

  public UserConnectionService(UserService userService,
      UserConnectionRepository userConnectionRepository) {
    this.userService = userService;
    this.userConnectionRepository = userConnectionRepository;
  }

  public Pair<Optional<UserId>, String> invite(UserId inviterUserId, InviteCode inviteCode) {
    Optional<User> partner = userService.getUser(inviteCode);
    if (partner.isEmpty()) {
      log.info("Invalid invite code. {}, from {}", inviteCode, inviterUserId);
      return Pair.of(Optional.empty(), "Invalid invite code");
    }

    UserId partnerUserId = partner.get().userId();
    String partnerUsername = partner.get().username();
    if (partnerUserId.equals(inviterUserId)) {
      return Pair.of(Optional.empty(), "Can't self invite");
    }

    UserConnectionsStatus status = getStatus(inviterUserId, partnerUserId);

    return switch (status) {
      case NONE, DISCONNECTED -> {
        Optional<String> inviterUsername = userService.getUsername(inviterUserId);
        if (inviterUsername.isEmpty()) {
          log.warn("InviteRequest failed.");
          yield Pair.of(Optional.of(partnerUserId), "InviteRequest failed");
        }

        try {
          setStatus(inviterUserId, partnerUserId, UserConnectionsStatus.PENDING);
          yield Pair.of(Optional.of(partnerUserId), inviterUsername.get());
        } catch (Exception ex) {
          log.error("Set pending failed. cause: {}", ex.getMessage());
          yield Pair.of(Optional.empty(), "InviteRequest failed");
        }
      }
      case ACCEPTED ->
          Pair.of(Optional.of(partnerUserId), "Already connected with " + partnerUsername);
      case PENDING, REJECTED -> {
        log.info("{} invites {} but does not delivet invitation request.",
            inviterUserId,
            partnerUsername);
        yield Pair.of(Optional.of(partnerUserId), "Already invited to " + partnerUsername);
      }
    };
  }

  private UserConnectionsStatus getStatus(UserId inviterUserId, UserId partnerUserId) {
    return userConnectionRepository.findStatusByPartnerAUserIdAndPartnerBUserId(
            Long.min(inviterUserId.id(), partnerUserId.id()),
            Long.max(inviterUserId.id(), partnerUserId.id()))
        .map(projection -> UserConnectionsStatus.valueOf(projection.getStatus()))
        .orElse(UserConnectionsStatus.NONE);
  }

  @Transactional
  private void setStatus(UserId inviterUserId, UserId partnerUserId, UserConnectionsStatus status) {
    if (status == UserConnectionsStatus.ACCEPTED) {
      throw new IllegalArgumentException("Can't set to accepted.");
    }

    userConnectionRepository.save(new UserConnectionEntity(
        Long.min(inviterUserId.id(), partnerUserId.id()),
        Long.max(inviterUserId.id(), partnerUserId.id()),
        status,
        inviterUserId.id()
    ));
  }
}
