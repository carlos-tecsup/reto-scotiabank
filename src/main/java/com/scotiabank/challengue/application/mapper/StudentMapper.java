package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.StudentDTO;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.scotiabank.challengue.domain.model.StudentModel;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.FIELD;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE, injectionStrategy = FIELD)
public interface StudentMapper {


    // DOMAIN → DTO
    StudentDTO toDTO(StudentModel model);

    StudentModel fromDTO(StudentDTO dto);


    // DOMAIN → ENTITY
    StudentEntity toEntity(StudentModel model);

    // ENTITY → DOMAIN
    StudentModel toDomain(StudentEntity entity);


}