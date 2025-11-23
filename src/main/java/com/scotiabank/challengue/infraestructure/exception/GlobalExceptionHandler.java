package com.scotiabank.challengue.infraestructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

        public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                      WebProperties.Resources resources,
                                      ApplicationContext applicationContext,
                                      ServerCodecConfigurer configurer) {
            super(errorAttributes, resources, applicationContext);
            setMessageWriters(configurer.getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest req) {
            Throwable error = getError(req);
            log.error(">>> GlobalExceptionHandler error type: " + error.getMessage());
            Map<String, Object> body = new HashMap<>();

            if (error instanceof ValidationException ve) {
                body.put("code", "validation_error");
                body.put("errors", ve.getErrors());
            } else {
                body.put("code", "business_error");
                body.put("error", error.getMessage());
            }

            return ServerResponse
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body));
        }
}

