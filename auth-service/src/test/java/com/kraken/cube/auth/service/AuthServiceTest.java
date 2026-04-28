package com.kraken.cube.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kraken.cube.auth.entity.Role;
import com.kraken.cube.auth.entity.UserAuth;
import com.kraken.cube.auth.repository.RoleRepository;
import com.kraken.cube.auth.repository.UserAuthRepository;
import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.outboxmessage.OutboxSender;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock private UserAuthRepository userAuthRepo;
    @Mock private RoleRepository roleRepo;
    @Mock private OutboxSender outboxSender;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegUserAndSendMessage() throws Exception{

        String rawPassword = "password";
        String encodedPassword = "en_password";
        String name = "Kraken";
        String username = "Kraken_d";
        String surname = "Krakenov";
        String patronymic = "Krakend";
        String phone = "Phone";

        Long generatedId = 100L;

        when(userAuthRepo.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        when(roleRepo.getUserRole()).thenReturn(new Role(1L,"ROLE_USER", ""));

        when(userAuthRepo.save(any(UserAuth.class))).thenAnswer(invocation -> {
            UserAuth user = invocation.getArgument(0);
            user.setId(generatedId);
            return user;
        });


        
        authService.register(username, name, surname, patronymic, phone, rawPassword);
        
        verify(passwordEncoder).encode(rawPassword);

        ArgumentCaptor<UserAuth> captor = ArgumentCaptor.forClass(UserAuth.class);

        verify(userAuthRepo).save(captor.capture());

        UserAuth auth = captor.getValue();

        assertSoftly(softly -> {
            softly.assertThat(auth.getUsername()).isEqualTo(username);
            softly.assertThat(auth.getPasswordHash()).isEqualTo(encodedPassword);
        });

        ArgumentCaptor<UserRegisteredDto> regCapture = ArgumentCaptor.forClass(UserRegisteredDto.class);

        verify(outboxSender).send(eq("user-topic"), eq(generatedId.toString()), regCapture.capture());

        UserRegisteredDto regDto = regCapture.getValue();

        assertSoftly(softly -> {
            softly.assertThat(name).isEqualTo(regDto.getName());
            softly.assertThat(username).isEqualTo(regDto.getUsername());
            softly.assertThat(surname).isEqualTo(regDto.getSurname());
            softly.assertThat(patronymic).isEqualTo(regDto.getPatronymic());
            softly.assertThat(phone).isEqualTo(regDto.getPhone());
            softly.assertThat(generatedId).isEqualTo(regDto.getAuthId());
        });
    }
}
