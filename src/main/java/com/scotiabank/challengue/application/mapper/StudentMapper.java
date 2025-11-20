package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.StudentDTO;
import com.scotiabank.challengue.infraestructure.output.persistence.entity.StudentEntity;
import domain.model.StudentModel;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.FIELD;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE, injectionStrategy = FIELD)
public interface StudentMapper {

    // DTO → DOMAIN
    @Mapping(target = "status", expression = "java(mapActiveToStatus(dto.isActive()))")
    StudentModel toDomain(StudentDTO dto);

    // DOMAIN → DTO
    @Mapping(target = "isActive", expression = "java(mapStatusToActive(model.status()))")
    StudentDTO toDTO(StudentModel model);

    @Mapping(target = "status", expression = "java(dto.isActive() ? \"activo\" : \"inactivo\")")
    StudentModel fromDTO(StudentDTO dto);


    // DOMAIN → ENTITY
    StudentEntity toEntity(StudentModel model);

    // ENTITY → DOMAIN
    StudentModel toDomain(StudentEntity entity);

    default boolean mapStatusToActive(String status) {
        return "activo".equalsIgnoreCase(status);
    }

    default String mapActiveToStatus(boolean active) {
        return active ? "activo" : "inactivo";
    }

}