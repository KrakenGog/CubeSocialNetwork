package com.kraken.cube.outboxmessage;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableScheduling
@ConditionalOnProperty(name = "outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxAutoConfiguration {
    @Bean
    public OutboxProcessor outboxProcessor(
            OutboxRepository outboxRepository, 
            KafkaTemplate<String, String> kafkaTemplate) {
        return new OutboxProcessor(outboxRepository, kafkaTemplate);
    }

    @Bean
    OutboxSender outboxSender(OutboxRepository outboxRepository, ObjectMapper mapper){
        return new OutboxSender(outboxRepository, mapper);
    }
}
