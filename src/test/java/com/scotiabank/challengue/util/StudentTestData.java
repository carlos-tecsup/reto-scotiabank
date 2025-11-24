package com.scotiabank.challengue.util;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;

public final class StudentTestData {

    private StudentTestData() {
    }

    public static StudentModel activeStudentModel() {
        return StudentModel.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status(StatusEnum.ACTIVE.getDesc())
                .age(25)
                .build();
    }

    public static StudentModel inactiveStudentModel() {
        return StudentModel.builder()
                .id(2L)
                .name("Maria")
                .lastName("Lopez")
                .status(StatusEnum.INACTIVE.getDesc())
                .age(30)
                .build();
    }

    public static StudentEntity activeStudentEntity() {
        return StudentEntity.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status(StatusEnum.ACTIVE.getDesc())
                .age(25)
                .build();
    }

    public static StudentEntity activeStudentEntity2() {
        return StudentEntity.builder()
                .id(2L)
                .name("Carlos")
                .lastName("Garcia")
                .status(StatusEnum.ACTIVE.getDesc())
                .age(28)
                .build();
    }

    public static StudentEntity inactiveStudentEntity() {
        return StudentEntity.builder()
                .id(3L)
                .name("Maria")
                .lastName("Lopez")
                .status(StatusEnum.INACTIVE.getDesc())
                .age(30)
                .build();
    }

    public static CreateStudentRequestDTO activeCreateRequestDTO() {
        return CreateStudentRequestDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .isActive(true)
                .age(25)
                .build();
    }

    public static CreateStudentRequestDTO inactiveCreateRequestDTO() {
        return CreateStudentRequestDTO.builder()
                .id(2L)
                .name("Maria")
                .lastName("Lopez")
                .isActive(false)
                .age(30)
                .build();
    }

    public static CreateStudentRequestDTO nullActiveCreateRequestDTO() {
        return CreateStudentRequestDTO.builder()
                .id(3L)
                .name("Pedro")
                .lastName("Garcia")
                .isActive(null)
                .age(28)
                .build();
    }

    public static SearchStudentsRequestDTO activeSearchRequestDTO() {
        return SearchStudentsRequestDTO.builder()
                .isActive(true)
                .build();
    }

  public static SearchStudentsRequestDTO allSearchRequestDTO() {
    return SearchStudentsRequestDTO.builder()
            .isActive(null)
            .build();
}
    public static BaseStudentDTO activeBaseStudentDTO() {
        return BaseStudentDTO.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .status(StatusEnum.ACTIVE.getDesc())
                .age(25)
                .build();
    }

    public static BaseStudentDTO inactiveBaseStudentDTO() {
        return BaseStudentDTO.builder()
                .id(2L)
                .name("Maria")
                .lastName("Lopez")
                .status(StatusEnum.INACTIVE.getDesc())
                .age(30)
                .build();
    }
}
