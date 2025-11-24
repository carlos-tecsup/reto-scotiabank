package com.scotiabank.challengue.infraestructure.adapters.output.persistence.adapter;

import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.adapter.StudentRepositoryAdapter;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.entity.StudentEntity;
import com.scotiabank.challengue.util.StudentTestData;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for StudentRepositoryAdapter.
 */
@ExtendWith(MockitoExtension.class)
class StudentRepositoryAdapterTest {

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @Mock
    private R2dbcEntityTemplate template;

    @InjectMocks
    private StudentRepositoryAdapter studentRepositoryAdapter;

    private StudentModel activeStudentModel;
    private StudentModel inactiveStudentModel;
    private StudentEntity activeStudentEntity;
    private StudentEntity inactiveStudentEntity;

    @BeforeEach
    void setUp() {
        activeStudentModel = StudentTestData.activeStudentModel();

        inactiveStudentModel = StudentTestData.inactiveStudentModel();

        activeStudentEntity = StudentTestData.activeStudentEntity();

        inactiveStudentEntity = StudentTestData.inactiveStudentEntity();
    }

    @Test
    void save_ShouldPersistEntity_WhenValidEntity() {
        // Arrange
        when(studentMapper.toEntity(activeStudentModel)).thenReturn(activeStudentEntity);
        when(template.insert(activeStudentEntity)).thenReturn(Mono.just(activeStudentEntity));
        when(studentMapper.toDomain(activeStudentEntity)).thenReturn(activeStudentModel);

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.save(activeStudentModel))
                .verifyComplete();

        verify(studentMapper).toEntity(activeStudentModel);
        verify(template).insert(activeStudentEntity);
    }

    @Test
    void searchStudents_ShouldReturnAllStudents_WhenStatusIsNull() {
        // Arrange
        when(studentRepository.findAllByOrderByIdDesc()).thenReturn(Flux.just(activeStudentEntity, inactiveStudentEntity));
        when(studentMapper.toDomain(activeStudentEntity)).thenReturn(activeStudentModel);
        when(studentMapper.toDomain(inactiveStudentEntity)).thenReturn(inactiveStudentModel);

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.searchStudents(null))
                .expectNext(activeStudentModel)
                .expectNext(inactiveStudentModel)
                .verifyComplete();

        verify(studentRepository).findAllByOrderByIdDesc();
    }

    @Test
    void searchStudents_ShouldReturnFilteredStudents_WhenStatusIsProvided() {
        // Arrange
        when(studentRepository.findByStatusOrderByIdDesc(StatusEnum.ACTIVE.getValue())).thenReturn(Flux.just(activeStudentEntity));
        when(studentMapper.toDomain(activeStudentEntity)).thenReturn(activeStudentModel);

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.searchStudents(StatusEnum.ACTIVE.getValue()))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.status()).isEqualTo(StatusEnum.ACTIVE.getValue());
                })
                .verifyComplete();

        verify(studentRepository).findByStatusOrderByIdDesc(StatusEnum.ACTIVE.getValue());
    }

    @Test
    void searchStudents_ShouldReturnEmptyFlux_WhenNoStudentsFound() {
        // Arrange
        when(studentRepository.findByStatusOrderByIdDesc(StatusEnum.ACTIVE.getValue())).thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.searchStudents(StatusEnum.ACTIVE.getValue()))
                .verifyComplete();

        verify(studentRepository).findByStatusOrderByIdDesc(StatusEnum.ACTIVE.getValue());
    }

    @Test
    void existsById_ShouldReturnTrue_WhenStudentExists() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.existsById(1L))
                .expectNext(true)
                .verifyComplete();

        verify(studentRepository).existsById(1L);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenStudentDoesNotExist() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(studentRepositoryAdapter.existsById(1L))
                .expectNext(false)
                .verifyComplete();

        verify(studentRepository).existsById(1L);
    }
}
