package com.kraken.cube.userService.listeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.userService.dto.UserDto;
import com.kraken.cube.userService.repository.UserRepository;
import com.kraken.cube.userService.service.UserService;
import static org.awaitility.Awaitility.await;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
public class RegistrationFlowIT {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;
    @MockitoSpyBean
    private UserRegisteredConsumer consumer;
    @Autowired
    private UserRepository userRepository;
    @MockitoSpyBean
    private UserService userService;

    @BeforeEach
    private void setup(){
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveAlreadyRegisteredUser() throws JsonProcessingException{
        UserRegisteredDto userRegisterdDto = new UserRegisteredDto(
            1L, "Kraken", "Kraken_n", "Kraken_s", "Kraken_p", "+375"
        );
        kafkaTemplate.send("user-topic","1", mapper.writeValueAsString(userRegisterdDto));


        ArgumentCaptor<UserRegisteredDto> dtoCaptor = ArgumentCaptor.forClass(UserRegisteredDto.class);
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(consumer).consume(dtoCaptor.capture());
            assertThat(userRegisterdDto).isEqualTo(dtoCaptor.getValue());
            ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
            verify(userService).createUser(userDtoCaptor.capture());
            assertThat(userDtoCaptor.getValue().getName()).isEqualTo("Kraken_n");
            assertThat(userRepository.count()).isEqualTo(1);
        });
    }
}
