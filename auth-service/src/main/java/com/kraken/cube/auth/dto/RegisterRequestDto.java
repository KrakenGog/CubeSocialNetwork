package com.kraken.cube.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String password;
}
