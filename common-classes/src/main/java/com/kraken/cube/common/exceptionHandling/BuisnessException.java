package com.kraken.cube.common.exceptionHandling;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuisnessException extends Exception {
    private String status;
    private HttpStatus httpStatus;
}
