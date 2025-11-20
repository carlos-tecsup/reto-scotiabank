package com.scotiabank.challengue.infraestructure.output.persistence.adapters.input.config;

import com.scotiabank.challengue.infraestructure.output.persistence.adapters.input.rest.StudentHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class RouterConfig {

    private static final String BASE = "/api/students";

    @Bean
    public RouterFunction<ServerResponse> studentRoutes(StudentHandler handler) {

        log.info(">>> HANDLER INVOCADO. Headers: {}", handler);

        return RouterFunctions.route(
                RequestPredicates.POST("/api/students/create")
                        .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)),
                handler::registerStudent
        );
    }


}
