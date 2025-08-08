package com.ddoongs.chatting.auth;

import com.ddoongs.chatting.entity.UserEntity;
import com.ddoongs.chatting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChatUserDetailsService implements UserDetailsService {

  private static final Logger log = LoggerFactory.getLogger(ChatUserDetailsService.class);

  private final UserRepository userRepository;

  public ChatUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.info("User not found: {}", username);
          return
              new UsernameNotFoundException("");
        });

    return new ChatUserDetails(
        userEntity.getUserId(),
        userEntity.getUsername(),
        userEntity.getPassword()
    );
  }
}
