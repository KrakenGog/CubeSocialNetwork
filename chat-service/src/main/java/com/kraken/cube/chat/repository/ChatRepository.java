package com.kraken.cube.chat.repository;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kraken.cube.chat.entity.Chat;

public interface ChatRepository extends CrudRepository<Chat, Long>{
    Optional<Chat> findById(Long chatId);
}
