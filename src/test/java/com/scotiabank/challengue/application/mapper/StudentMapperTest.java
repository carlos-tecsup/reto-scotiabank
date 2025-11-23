package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.scotiabank.challengue.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for StudentMapper.
 */
class StudentMapperTest {

    private StudentMapper mapper;

    private StudentModel activeStudentModel;
    private CreateStudentRequestDTO activeRequestDTO;
    private CreateStudentRequestDTO inactiveRequestDTO;
    private CreateStudentRequestDTO nullActiveRequestDTO;
    private StudentEntity studentEntity;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(StudentMapper.class);

        activeStudentModel = StudentTestData.activeStudentModel();

        activeRequestDTO = StudentTestData.activeCreateRequestDTO();

        inactiveRequestDTO = StudentTestData.inactiveCreateRequestDTO();

        nullActiveRequestDTO = StudentTestData.nullActiveCreateRequestDTO();

        studentEntity = StudentTestData.activeStudentEntity();
    }

    @Test
    void toBaseStudentDTO_ShouldMapCorrectly() {
        // Act
        BaseStudentDTO result = mapper.toBaseStudentDTO(activeStudentModel);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Juan");
        assertThat(result.getLastName()).isEqualTo("Perez");
        assertThat(result.getStatus()).isEqualTo("activo");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    void fromCreateRequestDTO_ShouldMapCorrectly_WhenIsActiveIsTrue() {
        // Act
        StudentModel result = mapper.fromCreateRequestDTO(activeRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Juan");
        assertThat(result.lastName()).isEqualTo("Perez");
        assertThat(result.status()).isEqualTo("activo");
        assertThat(result.age()).isEqualTo(25);
    }

    @Test
    void fromCreateRequestDTO_ShouldMapCorrectly_WhenIsActiveIsFalse() {
        // Act
        StudentModel result = mapper.fromCreateRequestDTO(inactiveRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("Maria");
        assertThat(result.lastName()).isEqualTo("Lopez");
        assertThat(result.status()).isEqualTo("inactivo");
        assertThat(result.age()).isEqualTo(30);
    }

    @Test
    void fromCreateRequestDTO_ShouldMapCorrectly_WhenIsActiveIsNull() {
        // Act
        StudentModel result = mapper.fromCreateRequestDTO(nullActiveRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.name()).isEqualTo("Pedro");
        assertThat(result.lastName()).isEqualTo("Garcia");
        assertThat(result.status()).isEqualTo("inactivo");
        assertThat(result.age()).isEqualTo(28);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {

        // Act
        StudentEntity result = mapper.toEntity(activeStudentModel);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Juan");
        assertThat(result.getLastName()).isEqualTo("Perez");
        assertThat(result.getStatus()).isEqualTo("activo");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    void toDomain_ShouldMapCorrectly() {

        // Act
        StudentModel result = mapper.toDomain(studentEntity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Juan");
        assertThat(result.lastName()).isEqualTo("Perez");
        assertThat(result.status()).isEqualTo("activo");
        assertThat(result.age()).isEqualTo(25);
    }

    @Test
    void bidirectionalMapping_ShouldPreserveData() {

        // Act
        StudentEntity entity = mapper.toEntity(activeStudentModel);
        StudentModel resultModel = mapper.toDomain(entity);

        // Assert
        assertThat(resultModel).isNotNull();
        assertThat(resultModel.id()).isEqualTo(activeStudentModel.id());
        assertThat(resultModel.name()).isEqualTo(activeStudentModel.name());
        assertThat(resultModel.lastName()).isEqualTo(activeStudentModel.lastName());
        assertThat(resultModel.status()).isEqualTo(activeStudentModel.status());
        assertThat(resultModel.age()).isEqualTo(activeStudentModel.age());
    }
}
