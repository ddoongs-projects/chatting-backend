package com.ddoongs.chatting.repository;

import com.ddoongs.chatting.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

}
