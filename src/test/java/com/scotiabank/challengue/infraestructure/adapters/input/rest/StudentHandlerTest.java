package com.scotiabank.challengue.infraestructure.adapters.input.rest;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.infraestructure.adapters.validator.RequestValidator;
import com.scotiabank.challengue.mock.StudentTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

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

    private CreateStudentRequestDTO createStudentRequest;
    private SearchStudentsRequestDTO searchStudentsRequest;
    private SearchStudentResponseDTO searchStudentResponse;
    private SearchStudentResponseDTO emptySearchResponse;

    @BeforeEach
    void setUp() throws IOException {
        createStudentRequest = StudentTestDataFactory.getInstance().getCreateStudentRequestMock();
        searchStudentsRequest = StudentTestDataFactory.getInstance().getSearchStudentsRequestMock();
        searchStudentResponse = StudentTestDataFactory.getInstance().getSearchStudentsResponseMock();
        
        emptySearchResponse = SearchStudentResponseDTO.builder()
                .students(List.of())
                .build();
    }

    @Test
    void registerStudent_ShouldReturnNoContent_WhenValidRequest() {
        // Arrange
        when(serverRequest.bodyToMono(CreateStudentRequestDTO.class))
                .thenReturn(Mono.just(createStudentRequest));
        when(requestValidator.validate(createStudentRequest))
                .thenReturn(Mono.just(createStudentRequest));
        when(studentUseCase.createStudent(createStudentRequest))
                .thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = studentHandler.registerStudent(serverRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenValidRequest() {
        // Arrange
        when(serverRequest.bodyToMono(SearchStudentsRequestDTO.class))
                .thenReturn(Mono.just(searchStudentsRequest));
        when(studentUseCase.searchStudents(searchStudentsRequest))
                .thenReturn(Mono.just(searchStudentResponse));

        // Act
        Mono<ServerResponse> result = studentHandler.searchStudents(serverRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenEmptyRequest() {
        // Arrange
        SearchStudentsRequestDTO emptyRequest = SearchStudentsRequestDTO.builder().build();
        
        when(serverRequest.bodyToMono(SearchStudentsRequestDTO.class))
                .thenReturn(Mono.empty());
        when(studentUseCase.searchStudents(emptyRequest))
                .thenReturn(Mono.just(emptySearchResponse));

        // Act
        Mono<ServerResponse> result = studentHandler.searchStudents(serverRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
