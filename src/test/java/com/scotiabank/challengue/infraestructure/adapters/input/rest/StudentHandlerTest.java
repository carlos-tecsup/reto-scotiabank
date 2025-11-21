package com.scotiabank.challengue.infraestructure.adapters.input.rest;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.infraestructure.adapters.validator.RequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for StudentHandler.
 */
@ExtendWith(MockitoExtension.class)
class StudentHandlerTest {

    @Mock
    private StudentUseCase studentUseCase;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private StudentHandler studentHandler;

    @Test
    void registerStudent_ShouldReturnNoContent_WhenValidRequest() {
        // Given
        CreateStudentRequestDTO request = CreateStudentRequestDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .isActive(true)
                .age(25)
                .build();

        when(serverRequest.bodyToMono(CreateStudentRequestDTO.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(CreateStudentRequestDTO.class))).thenReturn(Mono.just(request));
        when(studentUseCase.createStudent(any(CreateStudentRequestDTO.class))).thenReturn(Mono.empty());

        // When
        Mono<ServerResponse> result = studentHandler.registerStudent(serverRequest);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenValidRequest() {
        // Given
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(true)
                .build();

        BaseStudentDTO student = BaseStudentDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status("activo")
                .age(25)
                .build();

        SearchStudentResponseDTO response = SearchStudentResponseDTO.builder()
                .students(List.of(student))
                .build();

        when(serverRequest.bodyToMono(SearchStudentsRequestDTO.class)).thenReturn(Mono.just(request));
        when(studentUseCase.searchStudents(any(SearchStudentsRequestDTO.class))).thenReturn(Mono.just(response));

        // When
        Mono<ServerResponse> result = studentHandler.searchStudents(serverRequest);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenEmptyRequest() {
        // Given
        SearchStudentResponseDTO response = SearchStudentResponseDTO.builder()
                .students(List.of())
                .build();

        when(serverRequest.bodyToMono(SearchStudentsRequestDTO.class)).thenReturn(Mono.empty());
        when(studentUseCase.searchStudents(any(SearchStudentsRequestDTO.class))).thenReturn(Mono.just(response));

        // When
        Mono<ServerResponse> result = studentHandler.searchStudents(serverRequest);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
