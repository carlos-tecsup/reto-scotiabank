package com.scotiabank.challengue.infraestructure.adapters.input.rest;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.infraestructure.adapters.validator.RequestValidator;
import com.scotiabank.challengue.infraestructure.config.RouterConfig;
import com.scotiabank.challengue.mock.StudentTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static com.scotiabank.challengue.application.constants.Constants.PATH_BASE;
import static org.mockito.Mockito.when;

/**
 * Integration tests for StudentHandler using WebTestClient.
 */
@ExtendWith(MockitoExtension.class)
class StudentHandlerTest {

    private WebTestClient webTestClient;

    @Mock
    private StudentUseCase studentUseCase;

    @Mock
    private RequestValidator requestValidator;

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

        StudentHandler handler = new StudentHandler(studentUseCase, requestValidator);
        RouterFunction<?> routes = new RouterConfig().studentRoutes(handler);

        this.webTestClient = WebTestClient.bindToRouterFunction(routes).build();

    }

    @Test
    void registerStudent_ShouldReturnNoContent_WhenValidRequest() {
        // Arrange
        when(requestValidator.validate(createStudentRequest))
                .thenReturn(Mono.just(createStudentRequest));
        when(studentUseCase.createStudent(createStudentRequest))
                .thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.post()
                .uri(PATH_BASE + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createStudentRequest)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenValidRequest() {
        // Arrange
        when(studentUseCase.searchStudents(searchStudentsRequest))
                .thenReturn(Mono.just(searchStudentResponse));

        // Act & Assert
        webTestClient.method(HttpMethod.GET)
                .uri(PATH_BASE + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(searchStudentsRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void searchStudents_ShouldReturnOk_WhenEmptyRequest() {
        // Arrange
        SearchStudentsRequestDTO emptyRequest = SearchStudentsRequestDTO.builder().build();
        
        when(studentUseCase.searchStudents(emptyRequest))
                .thenReturn(Mono.just(emptySearchResponse));

        // Act & Assert
        webTestClient.get()
                .uri(PATH_BASE + "/search")
                .exchange()
                .expectStatus().isOk()
                .expectBody(SearchStudentResponseDTO.class);
    }
}
