package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import com.scotiabank.challengue.mock.StudentTestDataFactory;
import com.scotiabank.challengue.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepositoryPort studentRepositoryPort;

    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @InjectMocks
    private StudentServiceImpl studentService;

        private CreateStudentRequestDTO createRequest;
        private StudentModel activeStudentDomain;
        private StudentModel inactiveStudentDomain;
        private BaseStudentDTO activeStudentDto;
        private BaseStudentDTO inactiveStudentDto;

    @BeforeEach
    void setUp() throws IOException {
        // Cargar datos de prueba desde JSON usando el factory
        createRequest = StudentTestDataFactory.getInstance().getCreateStudentRequestMock();

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
        when(studentRepositoryPort.save(any(StudentModel.class))).thenReturn(Mono.empty());

        // Act
        var result = studentService.createStudent(createRequest);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(studentRepositoryPort).existsById(1L);
        verify(studentRepositoryPort).save(any(StudentModel.class));
    }

    @Test
    void createStudent_ShouldThrowException_WhenStudentAlreadyExists() {
        // Arrange
        when(studentMapper.fromCreateRequestDTO(createRequest)).thenReturn(activeStudentDomain);
        when(studentRepositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        // Act
        var result = studentService.createStudent(createRequest);

        // Assert
        StepVerifier.create(result)
                .expectError(DuplicatedStudentException.class)
                .verify();

        verify(studentRepositoryPort).existsById(1L);
    }

    @Test
    void searchStudents_ShouldReturnAllStudents_WhenIsActiveIsNull() {
        // Arrange - Usar factory para cargar request desde JSON
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(null)
                .build();

        when(studentRepositoryPort.searchStudents(null))
                .thenReturn(Flux.just(activeStudentDomain, inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Act & Assert
        StepVerifier.create(studentService.searchStudents(request))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(2);
                    assertThat(result.getStudents()).containsExactly(activeStudentDto, inactiveStudentDto);
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(null);
    }

    @Test
    void searchStudents_ShouldReturnActiveStudents_WhenIsActiveIsTrue() throws IOException {
        // Arrange - Usar factory para cargar request desde JSON
        SearchStudentsRequestDTO request = StudentTestDataFactory.getInstance().getSearchStudentsRequestMock();

        when(studentRepositoryPort.searchStudents(StatusEnum.ACTIVE.getDesc()))
                .thenReturn(Flux.just(activeStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);

        // Act & Assert
        StepVerifier.create(studentService.searchStudents(request))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(1);
                    assertThat(result.getStudents().get(0).getStatus()).isEqualTo(StatusEnum.ACTIVE.getDesc());
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.ACTIVE.getDesc());
    }

    @Test
    void searchStudents_ShouldReturnInactiveStudents_WhenIsActiveIsFalse() throws IOException {
        // Arrange - Construir request con isActive=false
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(false)
                .build();

        when(studentRepositoryPort.searchStudents(StatusEnum.INACTIVE.getDesc()))
                .thenReturn(Flux.just(inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Act & Assert
        StepVerifier.create(studentService.searchStudents(request))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).hasSize(1);
                    assertThat(result.getStudents().get(0).getStatus()).isEqualTo(StatusEnum.INACTIVE.getDesc());
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.INACTIVE.getDesc());
    }

    @Test
    void searchStudents_ShouldReturnEmptyList_WhenNoStudentsFound() throws IOException {
        // Arrange - Usar factory para cargar request desde JSON
        SearchStudentsRequestDTO request = StudentTestDataFactory.getInstance().getSearchStudentsRequestMock();

        when(studentRepositoryPort.searchStudents(StatusEnum.ACTIVE.getDesc()))
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(studentService.searchStudents(request))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getStudents()).isEmpty();
                })
                .verifyComplete();

        verify(studentRepositoryPort).searchStudents(StatusEnum.ACTIVE.getDesc());
    }
}
