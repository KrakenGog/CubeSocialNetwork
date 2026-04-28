package com.kraken.cube.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisteredDto {
    private Long authId;
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
}
