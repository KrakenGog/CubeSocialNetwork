package com.kraken.cube.common.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;
    private String path;
    private String buisnessStatus;


    public ExceptionResponse(HttpStatus status, String message, String path, String buisnessStatus){
        this.time = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.buisnessStatus = buisnessStatus;
    }
}
