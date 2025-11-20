package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.StudentDTO;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
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
        return Mono.defer(() -> {
                    log.info("Executing registerStudent with ID: {}", studentDTO.getId());
                    StudentModel studentModel = studentMapper.fromDTO(studentDTO);

                    return studentRepositoryPort.existsById(studentModel.id())
                            .flatMap(exists -> exists
                                    ? Mono.error(new DuplicatedStudentException(studentModel.id()))
                                    : studentRepositoryPort.save(studentModel));
                })
                .doOnSuccess(v -> log.info("StudentServiceImpl registered successfully with ID: {}", studentDTO.getId()))
                .doOnError(e -> log.error("Error in registerStudent for ID: {}", studentDTO.getId(), e))
                .contextWrite(ctx -> ctx.put("studentId", studentDTO.getId()));
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
