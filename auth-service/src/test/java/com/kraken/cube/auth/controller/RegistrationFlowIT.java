package com.kraken.cube.auth.controller;

import org.apache.http.client.methods.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.cube.auth.dto.RegisterRequestDto;
import com.kraken.cube.auth.entity.Role;
import com.kraken.cube.auth.repository.RoleRepository;
import com.kraken.cube.auth.repository.UserAuthRepository;
import com.kraken.cube.outboxmessage.OutboxProcessor;
import com.kraken.cube.outboxmessage.OutboxRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
@ActiveProfiles("test")
public class RegistrationFlowIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private OutboxRepository outboxRepository;
    @Autowired
    private OutboxProcessor outboxProcessor;
    @Autowired
    private ObjectMapper mapper;

    @MockitoSpyBean
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @BeforeEach
    void testSetup() {
        userAuthRepository.deleteAll();
        outboxRepository.deleteAll();
        roleRepo.deleteAll();

        Role role = Role.builder()
                .name("ROLE_USER")
                .description("null")
                .build();

        roleRepo.saveAndFlush(role);
    }

    @Test
    void shouldRegisterUserAndSendKafkaMessage() throws Exception {
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .name("Kraken")
                .surname("Kraken_s")
                .password("password")
                .phone("phone")
                .username("Kraken_u")
                .build();

        String json = mapper.writeValueAsString(registerRequestDto);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isCreated());

        assertThat(outboxRepository.count()).isEqualTo(1);
        assertThat(userAuthRepository.count()).isEqualTo(1);

        outboxProcessor.handle();

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), (Object) payloadCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("user-topic");
        assertThat(keyCaptor.getValue()).isEqualTo("1");
        assertThat(payloadCaptor.getValue()).contains("Kraken");

        assertThat(outboxRepository.count()).isEqualTo(0);
    }
}
