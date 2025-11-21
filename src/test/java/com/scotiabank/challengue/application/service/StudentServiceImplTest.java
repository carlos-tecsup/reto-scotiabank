package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
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

import java.time.Duration;

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

    // ====== Arrange común (datos base) ======

    private CreateStudentRequestDTO createRequest;
    private StudentModel activeStudentDomain;
    private StudentModel inactiveStudentDomain;
    private BaseStudentDTO activeStudentDto;
    private BaseStudentDTO inactiveStudentDto;

    @BeforeEach
    void setUp() {
        // Estos datos forman parte del "Arrange global" reutilizable
        createRequest = CreateStudentRequestDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .isActive(true)
                .age(25)
                .build();

        activeStudentDomain = StudentModel.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status("activo")
                .age(25)
                .build();

        inactiveStudentDomain = StudentModel.builder()
                .id(2L)
                .name("Maria")
                .lastName("Lopez")
                .status("inactivo")
                .age(30)
                .build();

        activeStudentDto = BaseStudentDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status("activo")
                .age(25)
                .build();

        inactiveStudentDto = BaseStudentDTO.builder()
                .id(2L)
                .name("Maria")
                .lastName("Lopez")
                .status("inactivo")
                .age(30)
                .build();
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
        // Arrange
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(null)
                .build();

        when(studentRepositoryPort.searchStudents(null))
                .thenReturn(Flux.just(activeStudentDomain, inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Act
        SearchStudentResponseDTO result = studentService.searchStudents(request)
                .block(Duration.ofSeconds(5));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStudents()).hasSize(2);
        assertThat(result.getStudents()).containsExactly(activeStudentDto, inactiveStudentDto);
        verify(studentRepositoryPort).searchStudents(null);
    }

    @Test
    void searchStudents_ShouldReturnActiveStudents_WhenIsActiveIsTrue() {
        // Arrange
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(true)
                .build();

        when(studentRepositoryPort.searchStudents("activo"))
                .thenReturn(Flux.just(activeStudentDomain));
        when(studentMapper.toBaseStudentDTO(activeStudentDomain)).thenReturn(activeStudentDto);

        // Act
        SearchStudentResponseDTO result = studentService.searchStudents(request)
                .block(Duration.ofSeconds(5));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStudents()).hasSize(1);
        assertThat(result.getStudents().get(0).getStatus()).isEqualTo("activo");
        verify(studentRepositoryPort).searchStudents("activo");
    }

    @Test
    void searchStudents_ShouldReturnInactiveStudents_WhenIsActiveIsFalse() {
        // Arrange
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(false)
                .build();

        when(studentRepositoryPort.searchStudents("inactivo"))
                .thenReturn(Flux.just(inactiveStudentDomain));
        when(studentMapper.toBaseStudentDTO(inactiveStudentDomain)).thenReturn(inactiveStudentDto);

        // Act
        SearchStudentResponseDTO result = studentService.searchStudents(request)
                .block(Duration.ofSeconds(5));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStudents()).hasSize(1);
        assertThat(result.getStudents().get(0).getStatus()).isEqualTo("inactivo");
        verify(studentRepositoryPort).searchStudents("inactivo");
    }

    @Test
    void searchStudents_ShouldReturnEmptyList_WhenNoStudentsFound() {
        // Arrange
        SearchStudentsRequestDTO request = SearchStudentsRequestDTO.builder()
                .isActive(true)
                .build();

        when(studentRepositoryPort.searchStudents("activo"))
                .thenReturn(Flux.empty());

        // Act
        SearchStudentResponseDTO result = studentService.searchStudents(request)
                .block(Duration.ofSeconds(5));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStudents()).isEmpty();
        verify(studentRepositoryPort).searchStudents("activo");
    }
}
