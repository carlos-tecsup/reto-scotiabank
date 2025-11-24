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

    private StudentEntity activeStudentEntity1;
    private StudentEntity savedActiveStudent2;

    private StudentEntity savedActiveStudent1;
    private StudentEntity savedInactiveStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll().block();
        activeStudentEntity1 = StudentTestData.activeStudentEntity();
        savedActiveStudent1 = template.insert(StudentEntity.class).using(StudentTestData.activeStudentEntity2()).block();
        savedActiveStudent2 = template.insert(StudentEntity.class).using(StudentTestData.activeStudentEntity3()).block();
        savedInactiveStudent = template.insert(StudentEntity.class).using(StudentTestData.inactiveStudentEntity()).block();

    }

    @Test
    void save_ShouldPersistEntity() {
    // Arrange
    StepVerifier.create(template.insert(StudentEntity.class).using(activeStudentEntity1))
    .assertNext(savedStudent -> {
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isEqualTo(1L);
        assertThat(savedStudent.getName()).isEqualTo("Juan");
        assertThat(savedStudent.getStatus()).isEqualTo(StatusEnum.ACTIVE.getDesc());
    })
    .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnAllEntities() {
        // Act & Assert
        StepVerifier.create(studentRepository.findAllByOrderByIdDesc().collectList())
                .assertNext(students -> {
                    assertThat(students).isNotNull();
                    assertThat(students).hasSize(3);
                    
                    assertThat(students)
                            .filteredOn(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                            .hasSize(2)
                            .extracting(StudentEntity::getId)
                            .containsExactlyInAnyOrder(savedActiveStudent1.getId(), savedActiveStudent2.getId());
                    
                    assertThat(students)
                            .filteredOn(student -> StatusEnum.INACTIVE.getDesc().equals(student.getStatus()))
                            .hasSize(1)
                            .extracting(StudentEntity::getId)
                            .contains(savedInactiveStudent.getId());
                })
                .verifyComplete();
    }

    @Test
    void findByStatusOrderByIdDesc_ShouldReturnFilteredEntities() {

        // Act & Assert
        StepVerifier.create(studentRepository.findByStatusOrderByIdDesc(StatusEnum.ACTIVE.getDesc()))
                .expectNextMatches(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .expectNextMatches(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .thenConsumeWhile(student -> StatusEnum.ACTIVE.getDesc().equals(student.getStatus()))
                .verifyComplete();
    }

    @Test
    void findByStatusOrderByIdDesc_ShouldReturnEmpty_WhenNoMatch() {
        // Arrange
        String nonExistentStatus = "nonexistent";

        // Act & Assert
        StepVerifier.create(studentRepository.findByStatusOrderByIdDesc(nonExistentStatus))
                .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenEntityExists() {

        // Act & Assert
        StepVerifier.create(studentRepository.existsById(savedActiveStudent2.getId()))
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
}
