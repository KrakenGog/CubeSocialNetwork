package com.kraken.cube.userService.mapper;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import org.junit.jupiter.api.Test;

import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.userService.dto.UserDto;

public class UserMapperTest {
    private final UserMapper mapper = new UserMapperImpl();
    
    

    @Test
    void shouldMapUserRegisteredDtoToUserDto() {
        UserRegisteredDto userRegisteredDto = UserRegisteredDto.builder()
                .authId(10L)
                .name("Kraken")
                .patronymic("Krakenov")
                .phone("+375257511580")
                .surname("Krakend")
                .username("Kraken_go")
                .build();

        UserDto userDto = mapper.toUserDto(userRegisteredDto);

        assertSoftly(softly -> {
            softly.assertThat(userDto.getAuthId())
            .isEqualTo(userRegisteredDto.getAuthId());

            softly.assertThat(userDto.getName())
            .isEqualTo(userRegisteredDto.getName());

            softly.assertThat(userDto.getPatronymic())
            .isEqualTo(userRegisteredDto.getPatronymic());

            softly.assertThat(userDto.getPhone())
            .isEqualTo(userRegisteredDto.getPhone());

            softly.assertThat(userDto.getSurname())
            .isEqualTo(userRegisteredDto.getSurname());

            softly.assertThat(userDto.getUsername())
            .isEqualTo(userRegisteredDto.getUsername());
        });
    }
}
