package com.scotiabank.challengue.infraestructure.config;

import com.scotiabank.challengue.infraestructure.adapters.input.rest.StudentHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.scotiabank.challengue.application.constants.Constants.PATH_BASE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
@Slf4j
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> studentRoutes(StudentHandler handler) {
        return RouterFunctions.route()
                .nest(path(PATH_BASE), builder -> builder
                        .POST("/create", accept(MediaType.APPLICATION_JSON), handler::registerStudent)
                        .GET("/search", accept(MediaType.APPLICATION_JSON), handler::searchStudents))
                .build();
    }

}
