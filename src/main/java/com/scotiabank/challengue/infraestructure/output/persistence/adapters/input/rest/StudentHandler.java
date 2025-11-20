package com.scotiabank.challengue.infraestructure.output.persistence.adapters.input.rest;

import com.scotiabank.challengue.application.dto.StudentDTO;
import domain.ports.input.StudentUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;


@Slf4j
@Component
public class StudentHandler {
    private final StudentUseCase studentUseCase;

    public StudentHandler(StudentUseCase studentUseCase) {
        this.studentUseCase = studentUseCase;
    }

    public Mono<ServerResponse> registerStudent(ServerRequest request) {
        Mono<StudentDTO> monoCourseDTO = request.bodyToMono(StudentDTO.class);
        log.info("Registering student with data: {}", monoCourseDTO);
        return monoCourseDTO
                .flatMap(dto -> studentUseCase.createStudent(dto)
                        .doOnSuccess(v -> log.info("Student registered successfully with ID: {}", dto.getId()))
                        .thenReturn(dto))
                .flatMap(dto -> ServerResponse.created(URI.create("/api/students/" + dto.getId())).build())
                .onErrorResume(e -> {
                    log.error("Error registering student", e);
                    return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorPayload(e.getMessage()));
                });
    }
    private record ErrorResponse(String code, String message) {}

    private ErrorResponse errorPayload(String message) {
        return new ErrorResponse("registration_error", message);
    }
}
