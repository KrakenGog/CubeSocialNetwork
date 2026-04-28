package com.kraken.cube.outboxmessage;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OutboxProcessor {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public int handleCount = 10;

    @Scheduled(fixedDelayString = "${outbox.interval:1000}")
    @Transactional
    public void handle(){
        List<OutboxMessage> messages = outboxRepository.getFirstNMessage(handleCount);

        for (var message : messages) {
            kafkaTemplate.send(message.getTopic(), message.getAgregatedId(), message.getPayload());

            outboxRepository.delete(message);
        }
        
    }
}
