package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.StudentDTO;
import com.scotiabank.challengue.infraestructure.output.persistence.entity.StudentEntity;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.FIELD;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper (componentModel = SPRING, unmappedTargetPolicy = IGNORE,injectionStrategy = FIELD)
public interface StudentMapper {

    StudentEntity toEntity (StudentDTO studentDto);
    StudentDTO toDto (StudentEntity studentEntity);
}
