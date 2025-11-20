package com.scotiabank.challengue.infraestructure.output.persistence.repository;

import com.scotiabank.challengue.infraestructure.output.persistence.entity.StudentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaStudentRepository extends R2dbcRepository<StudentEntity, Long> {

}
