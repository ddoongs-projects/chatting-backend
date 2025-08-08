package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.entity.ChatUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long> {

  Optional<ChatUserEntity> findByUsername(@NonNull String username);

}
