package com.scotiabank.challengue.infraestructure.adapters.output.persistence.repository;

import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.scotiabank.challengue.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for StudentRepository.
 */
@DataR2dbcTest
@TestPropertySource(properties = {
    "spring.r2dbc.url=r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.r2dbc.username=sa",
    "spring.r2dbc.password="
})
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    private StudentEntity activeStudent1;
    private StudentEntity activeStudent2;
    private StudentEntity inactiveStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll().block();

        activeStudent1 = StudentTestData.activeStudentEntity();

        activeStudent2 = StudentTestData.activeStudentEntity2();

        inactiveStudent = StudentTestData.inactiveStudentEntity();
    }

    @Test
    void save_ShouldPersistEntity() {
                // Arrange: insertar usando R2dbcEntityTemplate (como en el código real)
                StepVerifier.create(template.insert(StudentEntity.class).using(activeStudent1))
                .assertNext(savedStudent -> {
                    assertThat(savedStudent).isNotNull();
                                        assertThat(savedStudent.getId()).isEqualTo(1L);
                    assertThat(savedStudent.getName()).isEqualTo("Juan");
                    assertThat(savedStudent.getStatus()).isEqualTo(StatusEnum.ACTIVE.getDesc());
                })
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEntity_WhenExists() {
                // Arrange: insertar usando R2dbcEntityTemplate
                StudentEntity savedStudent = template.insert(StudentEntity.class).using(activeStudent1).block();

                // Act & Assert
                StepVerifier.create(studentRepository.findById(savedStudent.getId()))
                                .assertNext(result -> {
                                        assertThat(result).isNotNull();
                                        assertThat(result.getId()).isEqualTo(savedStudent.getId());
                                        assertThat(result.getName()).isEqualTo("Juan");
                                })
                                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        Long nonExistentId = 99999L;

        // Act & Assert
        StepVerifier.create(studentRepository.findById(nonExistentId))
                .verifyComplete();
    }

    @Test
    void findByStatus_ShouldReturnFilteredEntities() {
        // Arrange
                template.insert(StudentEntity.class).using(activeStudent1).block();
                template.insert(StudentEntity.class).using(activeStudent2).block();
                template.insert(StudentEntity.class).using(inactiveStudent).block();

        // Act & Assert
        StepVerifier.create(studentRepository.findByStatus(StatusEnum.ACTIVE.getDesc()))
                .expectNextMatches(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .expectNextMatches(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .thenConsumeWhile(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .verifyComplete();
    }

    @Test
    void findByStatus_ShouldReturnEmpty_WhenNoMatch() {
        // Arrange
        String nonExistentStatus = "nonexistent";

        // Act & Assert
        StepVerifier.create(studentRepository.findByStatus(nonExistentStatus))
                .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenEntityExists() {
        // Arrange
                StudentEntity savedStudent = template.insert(StudentEntity.class).using(activeStudent1).block();

        // Act & Assert
        StepVerifier.create(studentRepository.existsById(savedStudent.getId()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenEntityDoesNotExist() {
        // Arrange
        Long nonExistentId = 99999L;

        // Act & Assert
        StepVerifier.create(studentRepository.existsById(nonExistentId))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void deleteById_ShouldRemoveEntity() {
        // Arrange
                StudentEntity savedStudent = template.insert(StudentEntity.class).using(activeStudent1).block();

        // Act
        StepVerifier.create(studentRepository.deleteById(savedStudent.getId()))
                .verifyComplete();

        // Assert
        StepVerifier.create(studentRepository.findById(savedStudent.getId()))
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnAllEntities() {
        // Arrange
                template.insert(StudentEntity.class).using(activeStudent1).block();
                template.insert(StudentEntity.class).using(inactiveStudent).block();

        // Act & Assert
        StepVerifier.create(studentRepository.findAll())
                .expectNextMatches(student -> student.getId() != null)
                .expectNextMatches(student -> student.getId() != null)
                .thenConsumeWhile(student -> student.getId() != null)
                .verifyComplete();
    }
}
