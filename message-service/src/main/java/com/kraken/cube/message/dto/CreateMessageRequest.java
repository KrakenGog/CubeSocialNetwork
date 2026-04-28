package com.kraken.cube.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMessageRequest (
    @NotBlank(message = "payload cant be empty") String payload,
    @NotNull(message = "chat id cant be null") Long chatId
) {
    
}
