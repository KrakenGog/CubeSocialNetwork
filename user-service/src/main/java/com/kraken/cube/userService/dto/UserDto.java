package com.kraken.cube.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private Long authId;
}
