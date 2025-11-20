package com.scotiabank.challengue.infraestructure.adapters.output.persistence.repository;

import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends R2dbcRepository<StudentEntity, Long> {


}
