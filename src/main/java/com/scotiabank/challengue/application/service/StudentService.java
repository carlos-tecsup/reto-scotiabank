package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
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


@Service
@Slf4j
public class StudentService implements StudentUseCase {
    private final StudentRepositoryPort studentRepositoryPort;
    private final StudentMapper studentMapper;
    
    public StudentService(
            @Qualifier("studentRepositoryAdapter") StudentRepositoryPort studentRepositoryPort, 
            StudentMapper studentMapper) {
        this.studentRepositoryPort = studentRepositoryPort;
        this.studentMapper = studentMapper;
    }

    @Override
    public Mono<Void> createStudent(CreateStudentRequestDTO createStudentRequestDTO) {
        log.info("Executing registerStudent with ID: {}", createStudentRequestDTO.getId());
        return Mono.defer(() -> {
                    StudentModel studentModel = studentMapper.fromCreateRequestDTO(createStudentRequestDTO);

                    return studentRepositoryPort.existsById(studentModel.id())
                            .flatMap(exists -> exists
                                    ? Mono.error(new DuplicatedStudentException(String.format(STUDENT_FOUND_MESSAGE, createStudentRequestDTO.getId())))
                                    : studentRepositoryPort.save(studentModel));
                })
                .doOnSuccess(v -> log.info("StudentService registered successfully with ID: {}", createStudentRequestDTO.getId()))
                .doOnError(e -> log.error("Error in registerStudent for ID: {}", createStudentRequestDTO.getId(), e))
                .then();
    }

    @Override
    public Mono<SearchStudentResponseDTO> searchStudents(SearchStudentsRequestDTO searchRequest) {
        log.info("Searching students with request: {}", searchRequest);
        
        String status = null;
        if (Objects.nonNull(searchRequest.getIsActive())) {
            status = searchRequest.getIsActive() ? StatusEnum.ACTIVE.getValue() : StatusEnum.INACTIVE.getValue();
        }
        
        return studentRepositoryPort.searchStudents(status)
                .map(studentMapper::toBaseStudentDTO)
                .collectList()
                .map(students -> SearchStudentResponseDTO.builder()
                        .students(students)
                        .build());
    }
}
