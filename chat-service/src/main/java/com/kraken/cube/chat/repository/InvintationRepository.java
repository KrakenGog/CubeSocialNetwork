package com.kraken.cube.chat.repository;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kraken.cube.chat.entity.Chat;
import com.kraken.cube.chat.entity.Invintation;

public interface InvintationRepository extends CrudRepository<Invintation, Long>{
    boolean existsByUserIdAndChatId(Long userId, Long chatId);
}
