package com.scotiabank.challengue.application.service;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.exceptions.DuplicatedStudentException;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.input.StudentUseCase;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.scotiabank.challengue.application.constants.Constants.STATUS_ACTIVE;
import static com.scotiabank.challengue.application.constants.Constants.STATUS_INACTIVE;

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
    public Mono<Void> createStudent(CreateStudentRequestDTO createStudentRequestDTO) {
        log.info("Executing registerStudent with ID: {}", createStudentRequestDTO.getId());
        return Mono.defer(() -> {
                    log.info("Executing registerStudent with ID: {}", createStudentRequestDTO.getId());
                    StudentModel studentModel = studentMapper.fromCreateRequestDTO(createStudentRequestDTO);

                    return studentRepositoryPort.existsById(studentModel.id())
                            .flatMap(exists -> exists
                                    ? Mono.error(new DuplicatedStudentException(studentModel.id()))
                                    : studentRepositoryPort.save(studentModel));
                })
                .doOnSuccess(v -> log.info("StudentServiceImpl registered successfully with ID: {}", createStudentRequestDTO.getId()))
                .doOnError(e -> log.error("Error in registerStudent for ID: {}", createStudentRequestDTO.getId(), e))
                .then();
    }

    @Override
    public Mono<SearchStudentResponseDTO> searchStudents(SearchStudentsRequestDTO searchRequest) {
        log.info("Searching students with request: {}", searchRequest);
        
        String status = null;
        if (Objects.nonNull(searchRequest.getIsActive())) {
            status = searchRequest.getIsActive() ? STATUS_ACTIVE : STATUS_INACTIVE;
        }
        
        return studentRepositoryPort.searchStudents(status)
                .map(studentMapper::toBaseStudentDTO)
                .collectList()
                .map(students -> SearchStudentResponseDTO.builder()
                        .students(students)
                        .build());
    }
}
