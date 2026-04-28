package com.kraken.cube.outboxmessage;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class OutboxSender {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;

    public void send(String topic, String id, Object payload) throws JsonProcessingException {
        String stringPayload = mapper.writeValueAsString(payload);

        outboxRepository.save(OutboxMessage.builder()
                .agregatedId(id)
                .createdAt(LocalDateTime.now())
                .payload(stringPayload)
                .topic(topic)
                .build());
    }
}
