package com.scotiabank.challengue.application.mapper;

import com.scotiabank.challengue.application.dto.BaseStudentDTO;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.util.StatusUtil;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.entity.StudentEntity;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.redis.entity.StudentRedisEntity;
import com.scotiabank.challengue.domain.model.StudentModel;
import org.mapstruct.*;

import static org.mapstruct.InjectionStrategy.FIELD;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE, injectionStrategy = FIELD, uses = StatusUtil.class)
public interface StudentMapper {


    // DOMAIN → BaseStudentDTO
    @Mapping(target = "status", source = "status", qualifiedByName = "fromCodeToValue")
    BaseStudentDTO toBaseStudentDTO(StudentModel model);

    // CreateStudentRequestDTO → DOMAIN
    @Mapping(target = "status", source = "isActive", qualifiedByName = "fromBooleanToStatus" )

    StudentModel fromCreateRequestDTO(CreateStudentRequestDTO dto);


    // DOMAIN → ENTITY
    StudentEntity toEntity(StudentModel model);

    // ENTITY → DOMAIN
    StudentModel toDomain(StudentEntity entity);

    // DOMAIN → REDIS ENTITY
    StudentRedisEntity toRedisEntity(StudentModel model);

    // REDIS ENTITY → DOMAIN
    StudentModel fromRedisEntity(StudentRedisEntity entity);

}