package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.scotiabank.challengue.domain.model.StudentModel;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.FIELD;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE, injectionStrategy = FIELD)
public interface StudentMapper {


    // DOMAIN → BaseStudentDTO
    @Mapping(target = "status", source = "status")
    BaseStudentDTO toBaseStudentDTO(StudentModel model);

    // CreateStudentRequestDTO → DOMAIN
    @Mapping(target = "status", expression = "java(dto.getIsActive() != null && dto.getIsActive() ? \"activo\" : \"inactivo\")")
    StudentModel fromCreateRequestDTO(CreateStudentRequestDTO dto);


    // DOMAIN → ENTITY
    StudentEntity toEntity(StudentModel model);

    // ENTITY → DOMAIN
    StudentModel toDomain(StudentEntity entity);


}