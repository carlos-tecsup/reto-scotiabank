package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import com.scotiabank.challengue.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepositoryPort studentRepositoryPort;

    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @InjectMocks
    private StudentService studentService;

    private CreateStudentRequestDTO createRequest;
    private SearchStudentsRequestDTO activeSearchRequestDTO;
    private SearchStudentsRequestDTO inactiveSearchRequestDTO;
    private SearchStudentsRequestDTO searchAllStudentsRequestDTO;

    private StudentModel activeStudentDomain;
    private StudentModel inactiveStudentDomain;
    private BaseStudentDTO activeStudentDto;
    private BaseStudentDTO inactiveStudentDto;

    @BeforeEach
    void setUp() {
        createRequest = StudentTestData.activeCreateRequestDTO();
        activeSearchRequestDTO = StudentTestData.activeSearchRequestDTO();
        inactiveSearchRequestDTO = StudentTestData.inactiveSearchRequestDTO();
        searchAllStudentsRequestDTO = StudentTestData.allSearchRequestDTO();

        activeStudentDomain = StudentTestData.activeStudentModel();
        inactiveStudentDomain = StudentTestData.inactiveStudentModel();

        activeStudentDto = StudentTestData.activeBaseStudentDTO();
        inactiveStudentDto = StudentTestData.inactiveBaseStudentDTO();
    }


    @Test
    void createStudent_ShouldReturnVoid_WhenValidRequest() {
        // Arrange
        when(studentMapper.fromCreateRequestDTO(createRequest)).thenReturn(activeStudentDomain);
        when(studentRepositoryPort.existsById(1L)).thenReturn(Mono.just(false));
        when(studentRepositoryPort.save(activeStudentDomain)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = studentService.createStudent(createRequest);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(studentRepositoryPort).existsById(1L);
        verify(studentRepositoryPort).save(activeStudentDomain);
    }

    @Test
    void createStudent_ShouldThrowException_WhenStudentAlreadyExists() {
        // Arrange
        when(studentMapper.fromCreateRequestDTO(createRequest)).thenReturn(activeStudentDomain);
        when(studentRepositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        // Act
        Mono<Void> result = studentService.createStudent(createRequest);

        // Assert
        StepVerifier.create(result)
                .expectError(DuplicatedStudentException.class)
                .verify();

        verify(studentRepositoryPort).existsById(1L);
    }

    @Test
    void searchStudents_ShouldReturnAllStudents_WhenIsActiveIsNull() {
        // Act
        when(studentRepositoryPort.searchStudents(null))
                .thenReturn(Flux.just(activeStudentDomain, inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Assert
        StepVerifier.create(studentService.searchStudents(searchAllStudentsRequestDTO))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(2);
                    assertThat(result.getStudents()).containsExactly(activeStudentDto, inactiveStudentDto);
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(null);
    }

    @Test
    void searchStudents_ShouldReturnActiveStudents_WhenIsActiveIsTrue() {
        // Act
        when(studentRepositoryPort.searchStudents(StatusEnum.ACTIVE.getValue()))
                .thenReturn(Flux.just(activeStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);

        // Assert
        StepVerifier.create(studentService.searchStudents(activeSearchRequestDTO))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(1);
                    assertThat(result.getStudents().get(0).getStatus()).isEqualTo(StatusEnum.ACTIVE.getValue());
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.ACTIVE.getValue());
    }

    @Test
    void searchStudents_ShouldReturnInactiveStudents_WhenIsActiveIsFalse() {
        // Act
        when(studentRepositoryPort.searchStudents(StatusEnum.INACTIVE.getValue()))
                .thenReturn(Flux.just(inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Assert
        StepVerifier.create(studentService.searchStudents(inactiveSearchRequestDTO))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(1);
                    assertThat(result.getStudents().get(0).getStatus()).isEqualTo(StatusEnum.INACTIVE.getValue());
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.INACTIVE.getValue());
    }

    @Test
    void searchStudents_ShouldReturnEmptyList_WhenNoStudentsFound() {
        // Act
        when(studentRepositoryPort.searchStudents(StatusEnum.ACTIVE.getValue()))
                .thenReturn(Flux.empty());

        // Assert
        StepVerifier.create(studentService.searchStudents(activeSearchRequestDTO))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).isEmpty();
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.ACTIVE.getValue());
    }
}
