package com.kraken.cube.common.exceptionHandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kraken.cube.common.dto.ExceptionResponse;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> createResponseEntity(
            @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        String message = "None";

        if (body instanceof ProblemDetail detail)
            message = detail.getDetail();

        String path = "None";

        if (request instanceof ServletWebRequest servletWebRequest)
            path = servletWebRequest.getRequest().getRequestURI();

        HttpStatus status = HttpStatus.resolve(statusCode.value());

        ExceptionResponse response = null;

        if (status != null)
            response = new ExceptionResponse(status, message, path, null);
        else
            response = new ExceptionResponse(LocalDateTime.now(), 0, "Unrecognized error", message, path, null);

        return new ResponseEntity<>(response, headers, statusCode);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleAllException(Exception ex, HttpServletRequest request) {
        
        return buildResponse(ex, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {
        return buildResponse(ex, request.getRequestURI(), HttpStatus.UNAUTHORIZED, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex,
            HttpServletRequest request) {
        return buildResponse(ex, request.getRequestURI(), HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(BuisnessException.class)
    public ResponseEntity<ExceptionResponse> handleBuisnessException(BuisnessException ex, HttpServletRequest request){
        return buildResponse(ex, request.getRequestURI(), ex.getHttpStatus(), ex.getStatus());
    }
    @PostConstruct
    public void init() {
        log.info("GlobalExceptionHandler succsessfuly connected");
    }

    

    private ResponseEntity<ExceptionResponse> buildResponse(Exception ex, String path,
            HttpStatus status, String buisnessStatus) {

        log.error("Exception in {}: {} - {}", path, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        
        if(status == null)
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        ExceptionResponse response = ExceptionResponse.builder()
                .error(ex.getClass().getName())
                .message(ex.getMessage())
                .status(status.value())
                .time(LocalDateTime.now())
                .path(path)
                .buisnessStatus(buisnessStatus)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
