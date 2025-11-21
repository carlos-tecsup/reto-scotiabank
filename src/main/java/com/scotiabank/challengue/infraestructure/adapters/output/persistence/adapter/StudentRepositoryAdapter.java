package com.scotiabank.challengue.infraestructure.adapters.output.persistence.adapter;

import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final R2dbcEntityTemplate template;


    public StudentRepositoryAdapter(StudentRepository studentRepository, StudentMapper studentMapper, R2dbcEntityTemplate template) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.template = template;
    }

    @Override
    public Mono<Void> save(StudentModel studentModel) {
        return Mono.defer(() -> {
                    log.info("Saving student with ID: {} in H2 database", studentModel.id());

                    StudentEntity entity = studentMapper.toEntity(studentModel);

                    return template.insert(entity)
                            .map(studentMapper::toDomain)
                            .doOnSuccess(saved ->
                                    log.info("StudentRepositoryAdapter Student saved successfully with ID: {}", saved.id()))
                            .doOnError(e ->
                                    log.error("Error saving student with ID: {}", studentModel.id(), e));
                }).then();
    }

    @Override
    public Flux<StudentModel> searchStudents(String status) {
        Flux<StudentEntity> query = (Objects.isNull(status)) ? studentRepository.findAll()
                                                             : studentRepository.findByStatus(status);
                
        return query.map(studentMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return studentRepository.existsById(id);
    }
}
