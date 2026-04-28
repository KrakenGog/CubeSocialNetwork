package com.kraken.cube.common.exceptionHandling;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kraken.cube.common.dto.ExceptionResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class GatewayExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
            ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE);

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, options);

        Throwable error = getError(request);

        int status = (int) errorPropertiesMap.getOrDefault("status", 500);
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null)
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        String message = (error != null && error.getMessage() != null)
                ? error.getMessage()
                : (String) errorPropertiesMap.getOrDefault("message", "No message available");

        ExceptionResponse responseBody = new ExceptionResponse(
                httpStatus,
                message,
                request.path());

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseBody);
    }

    @PostConstruct
    public void init() {
        log.info("GatewayExceptionHandler succsessfuly connected");
    }
}
