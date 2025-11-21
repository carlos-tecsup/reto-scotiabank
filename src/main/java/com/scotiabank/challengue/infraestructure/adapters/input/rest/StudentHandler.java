package com.scotiabank.challengue.infraestructure.adapters.input.rest;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.infraestructure.adapters.validator.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class StudentHandler {
    private final StudentUseCase studentUseCase;
    private final RequestValidator requestValidator;

    public StudentHandler(StudentUseCase studentUseCase, RequestValidator requestValidator) {
        this.studentUseCase = studentUseCase;
        this.requestValidator = requestValidator;
    }


    public Mono<ServerResponse> registerStudent(ServerRequest request) {
        return request.bodyToMono(CreateStudentRequestDTO.class)
                .flatMap(requestValidator:: validate)
                .flatMap(dto ->
                        studentUseCase.createStudent(dto)
                                .doOnSuccess(v -> log.info("StudentHandler Student registered successfully with ID: {}", dto.getId()))
                                .then(ServerResponse.noContent().build())
                );
    }

    public Mono<ServerResponse> searchStudents(ServerRequest request) {
        return request.bodyToMono(SearchStudentsRequestDTO.class)
                .defaultIfEmpty(SearchStudentsRequestDTO.builder().build())
                .flatMap(searchRequest -> {
                    log.info("Searching students with request: {}", searchRequest);
                    
                    return studentUseCase.searchStudents(searchRequest)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                });
    }

}
