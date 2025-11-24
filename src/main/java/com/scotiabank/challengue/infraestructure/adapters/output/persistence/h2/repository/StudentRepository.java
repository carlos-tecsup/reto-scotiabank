package com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.repository;

import com.scotiabank.challengue.infraestructure.adapters.output.persistence.h2.entity.StudentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface StudentRepository extends R2dbcRepository<StudentEntity, Long> {

    Flux<StudentEntity> findByStatusOrderByIdDesc(String status);


    Flux<StudentEntity> findAllByOrderByIdDesc();

}
