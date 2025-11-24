package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.enums.StatusEnum;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.scotiabank.challengue.application.constants.Constants.STUDENT_FOUND_MESSAGE;

@Service("redisStudentService")
@Slf4j
public class StudentRedisService implements StudentUseCase {
    
    private final StudentRepositoryPort studentRepositoryPort;
    private final StudentMapper studentMapper;

    public StudentRedisService(
            @Qualifier("redisStudentRepository") StudentRepositoryPort studentRepositoryPort,
            StudentMapper studentMapper) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.studentMapper = studentMapper;
    }

    @Override
    public Mono<Void> createStudent(CreateStudentRequestDTO request) {
        log.info("StudentRedisService - Creating student with ID: {} using Redis", request.getId());
        
        return studentRepositoryPort.existsById(request.getId())
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("StudentRedisService - Student with ID {} already exists in Redis", request.getId());
                        return Mono.error(new DuplicatedStudentException((String.format(STUDENT_FOUND_MESSAGE, request.getId()))));
                    }

                    String status = request.getIsActive() ? StatusEnum.ACTIVE.getCod() : StatusEnum.INACTIVE.getCod();
                    
                    StudentModel studentModel = StudentModel.builder()
                            .id(request.getId())
                            .name(request.getName())
                            .lastName(request.getLastName())
                            .status(status)
                            .age(request.getAge())
                            .build();

                    return studentRepositoryPort.save(studentModel);
                })
                .doOnSuccess(v -> log.info("StudentRedisService - Student created successfully with ID: {}", request.getId()))
                .doOnError(e -> log.error("StudentRedisService - Error creating student with ID: {}", request.getId(), e));
    }

    @Override
    public Mono<SearchStudentResponseDTO> searchStudents(SearchStudentsRequestDTO request) {
        log.info("StudentRedisService - Searching students with status: {} using Redis", request.getIsActive());
        
        String status = Objects.isNull(request.getIsActive()) ? null : 
                       request.getIsActive() ? StatusEnum.ACTIVE.getCod() : StatusEnum.INACTIVE.getCod();

        return studentRepositoryPort.searchStudents(status)
                .map(studentMapper::toBaseStudentDTO)
                .collectList()
                .map(students -> SearchStudentResponseDTO.builder()
                        .students(students)
                        .build())
                .doOnSuccess(response -> log.info("StudentRedisService - Found {} students in Redis", response.getStudents().size()))
                .doOnError(e -> log.error("StudentRedisService - Error searching students", e));
    }
}
