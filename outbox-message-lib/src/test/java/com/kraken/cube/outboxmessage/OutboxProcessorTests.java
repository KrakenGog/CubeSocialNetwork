package com.kraken.cube.outboxmessage;


import static org.mockito.Mockito.inOrder;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class OutboxProcessorTests {

    @Mock private OutboxRepository repo;
    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks private OutboxProcessor processor;

    @Test
    void shouldHandleTwoMessageInCorrectOrder(){
        OutboxMessage message1 = new OutboxMessage(1L,"topic", "payload1", LocalDateTime.of(1, 1, 1, 1, 1), false, "1");
        OutboxMessage message2 = new OutboxMessage(2L,"topic", "payload2", LocalDateTime.of(2, 1, 1, 1, 1), false, "1");
        

        when(repo.getFirstNMessage(2)).thenReturn(List.of(message1, message2));

        processor.handleCount = 2;
        processor.handle();

        InOrder order = inOrder(repo, kafkaTemplate);

        order.verify(kafkaTemplate).send("topic", "1", "payload1");
        order.verify(repo).delete(message1);

        order.verify(kafkaTemplate).send("topic", "1", "payload2");
        order.verify(repo).delete(message2);

        verifyNoMoreInteractions(repo, kafkaTemplate);
    }
}
