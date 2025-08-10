package com.ddoongs.chatting.service;

import com.ddoongs.chatting.constants.UserConnectionsStatus;
import com.ddoongs.chatting.dto.domain.InviteCode;
import com.ddoongs.chatting.dto.domain.User;
import com.ddoongs.chatting.dto.domain.UserId;
import com.ddoongs.chatting.dto.projection.UserIdUsernameInviterUserIdProjection;
import com.ddoongs.chatting.entity.UserConnectionEntity;
import com.ddoongs.chatting.repository.UserConnectionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
  private final UserConnectionLimitService userConnectionLimitService;

  public UserConnectionService(UserService userService,
      UserConnectionRepository userConnectionRepository,
      UserConnectionLimitService userConnectionLimitService) {
    this.userService = userService;
    this.userConnectionRepository = userConnectionRepository;
    this.userConnectionLimitService = userConnectionLimitService;
  }

  public List<User> getUsersByStatus(UserId userId, UserConnectionsStatus status) {
    List<UserIdUsernameInviterUserIdProjection> usersA = userConnectionRepository.findConnectionsByPartnerBUserIdAndStatus(
        userId.id(), status);
    List<UserIdUsernameInviterUserIdProjection> usersB = userConnectionRepository.findConnectionsByPartnerAUserIdAndStatus(
        userId.id(), status);

    if (status == UserConnectionsStatus.ACCEPTED) {
      return Stream.concat(usersA.stream(), usersB.stream())
          .map(item -> new User(new UserId(item.getUserId()), item.getUsername()))
          .toList();
    } else {
      return Stream.concat(usersA.stream(), usersB.stream())
          .filter(item -> !item.getInviterUserId().equals(userId.id()))
          .map(item -> new User(new UserId(item.getUserId()), item.getUsername()))
          .toList();
    }
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
        if (userService.getConnectionCount(inviterUserId)
            .filter(count -> count >= userConnectionLimitService.getLimitConnections())
            .isPresent()) {
          yield Pair.of(Optional.empty(), "Connection limit reached");
        }

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

  public Pair<Optional<UserId>, String> accept(UserId acceptorUserId, String inviterUsername) {
    Optional<UserId> userId = userService.getUserId(inviterUsername);

    if (userId.isEmpty()) {
      return Pair.of(Optional.empty(), "Invalid username");
    }

    UserId inviterUserId = userId.get();

    if (acceptorUserId.equals(inviterUserId)) {
      return Pair.of(Optional.empty(), "Can't self accept");
    }

    if (getInviterUserId(acceptorUserId, inviterUserId).filter(
        invitationSenderUserId -> invitationSenderUserId.equals(inviterUserId)).isEmpty()) {
      return Pair.of(Optional.empty(), "Invalid username");
    }

    UserConnectionsStatus status = getStatus(inviterUserId, acceptorUserId);
    if (status == UserConnectionsStatus.ACCEPTED) {
      return Pair.of(Optional.empty(), "Already connected");
    }
    if (status != UserConnectionsStatus.PENDING) {
      return Pair.of(Optional.empty(), "Accept failed");
    }

    Optional<String> acceptorUsername = userService.getUsername(acceptorUserId);
    if (acceptorUsername.isEmpty()) {
      log.error("Invalid userId. userId: {}", acceptorUserId);
      return Pair.of(Optional.empty(), "Accept failed");
    }

    try {
      userConnectionLimitService.accept(acceptorUserId, inviterUserId);
      return Pair.of(Optional.of(inviterUserId), acceptorUsername.get());
    } catch (EntityNotFoundException ex) {
      log.error("Accept failed. cause: {}", ex.getMessage());
      return Pair.of(Optional.empty(), "Accept failed");
    } catch (IllegalStateException ex) {
      return Pair.of(Optional.empty(), ex.getMessage());
    }
  }

  public Pair<Boolean, String> reject(UserId senderUserId, String inviterUsername) {
    return userService.getUserId(inviterUsername)
        .filter(inviterUserId -> !inviterUserId.equals(senderUserId))
        .filter(
            inviterUserId -> getInviterUserId(inviterUserId, senderUserId).map(
                invitationSenderUserId ->
                    invitationSenderUserId.equals(inviterUserId)).isPresent())
        .filter(inviterUserId -> getStatus(inviterUserId, senderUserId)
                                 == UserConnectionsStatus.PENDING)
        .map(inviterUserId -> {
          try {
            setStatus(inviterUserId, senderUserId, UserConnectionsStatus.REJECTED);
            return Pair.of(true, inviterUsername);
          } catch (Exception ex) {
            log.error("Set Reject failed. cause: {}", ex.getMessage());
            return Pair.of(false, "Reject failed");
          }
        }).orElse(Pair.of(false, "Reject failed"));
  }

  public Pair<Boolean, String> disconnect(UserId senderUserId, String partnerUsername) {
    return userService.getUserId(partnerUsername)
        .filter(partnerUserId -> !senderUserId.equals(partnerUserId))
        .map(partnerUserId -> {
          try {
            UserConnectionsStatus status = getStatus(senderUserId, partnerUserId);
            if (status == UserConnectionsStatus.ACCEPTED) {
              userConnectionLimitService.disconnect(senderUserId, partnerUserId);
              return Pair.of(true, partnerUsername);
            } else if (status == UserConnectionsStatus.REJECTED
                       && getInviterUserId(senderUserId, partnerUserId).filter(
                inviteUserId -> inviteUserId.equals(partnerUserId)).isPresent()) {
              setStatus(senderUserId, partnerUserId, UserConnectionsStatus.DISCONNECTED);
              return Pair.of(true, partnerUsername);
            }
          } catch (Exception ex) {
            log.error("Disconnect failed. cause: {}", ex.getMessage());
            return Pair.of(false, "Disconnect failed");
          }
          return Pair.of(false, "Disconnect failed");
        }).orElse(Pair.of(false, "Disconnect failed"));
  }

  private Optional<UserId> getInviterUserId(UserId partnerAUserId, UserId partnerBUserId) {
    return userConnectionRepository.findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
        Long.min(partnerAUserId.id(), partnerBUserId.id()),
        Long.max(partnerAUserId.id(), partnerBUserId.id())
    ).map(inviterUserId -> new UserId(inviterUserId.getInviterUserId()));
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
