package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.StudentDTO;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import domain.exceptions.DuplicatedStudentException;
import domain.model.StudentModel;
import domain.ports.input.StudentUseCase;
import domain.ports.output.StudentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StudentServiceImpl implements StudentUseCase {
    private final StudentRepositoryPort studentRepositoryPort;
    private final StudentMapper studentMapper;
    public StudentServiceImpl(StudentRepositoryPort studentRepositoryPort, StudentMapper studentMapper) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.studentMapper = studentMapper;
    }

    @Override
    public Mono<Void> createStudent(StudentDTO studentDTO) {
        log.info("Executing registerStudent with ID: {}", studentDTO.getId());

        return Mono.fromCallable(() -> studentMapper.fromDTO(studentDTO))
                .flatMap(student -> studentRepositoryPort.existsById(student.id())
                        .flatMap(exists -> {
                            if (exists) {
                                log.error("Student with ID {} already exists", student.id());
                                return Mono.error(new DuplicatedStudentException(student.id()));
                            }
                            return studentRepositoryPort.save(student)
                                    .doOnSuccess(unused ->
                                            log.info("Student with ID {} registered successfully", student.id())
                                    )
                                    .doOnError(e -> log.error("Error in registerStudent {}", studentDTO.getName()));
                        }));
    }


    @Override
    public Flux<StudentDTO> getAllStudentsActive() {
        return null;
    }

    @Override
    public Flux<StudentDTO> getAllStudents() {
        return null;
    }
}
