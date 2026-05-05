package com.kraken.cube.chat.repository;

import org.springframework.data.repository.CrudRepository;

import com.kraken.cube.chat.entity.ChatParticipant;

public interface ChatParticipantRepository extends CrudRepository<ChatParticipant, Long> {
    boolean existsByUserIdAndChatId(Long userId, Long chatId);
}
