package com.scotiabank.challengue.infraestructure.output.persistence.adapters;

import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.infraestructure.output.persistence.repository.JpaStudentRepository;
import domain.model.StudentModel;
import domain.ports.output.StudentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final JpaStudentRepository jpaStudentRepository;
    private final StudentMapper studentMapper;

    public StudentRepositoryAdapter(JpaStudentRepository jpaStudentRepository, StudentMapper studentMapper) {
        this.jpaStudentRepository = jpaStudentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public Mono<Void> save(StudentModel studentModel) {
        return Mono.defer(() -> {
                    log.info("Saving student with ID: {} in H2 database", studentModel.id());
                    return Mono.just(studentModel)
                            .map(studentMapper::toEntity)
                            .flatMap(jpaStudentRepository::save)
                            .map(studentMapper::toDomain);
                })
                .doOnSuccess(saved -> log.info("Student saved successfully with ID: {}", saved.id()))
                .doOnError(e -> log.error("Error saving student with ID: {}", studentModel.id(), e)).then();
    }

    @Override
    public Flux<StudentModel> findAllStudents() {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return null;
    }
}
