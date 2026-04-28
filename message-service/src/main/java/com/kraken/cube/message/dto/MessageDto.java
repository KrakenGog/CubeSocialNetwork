package com.kraken.cube.message.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
   
    private Long id;
   
    private Long chatId;
    
    private String payload;
    
    private Long authorId;
}
