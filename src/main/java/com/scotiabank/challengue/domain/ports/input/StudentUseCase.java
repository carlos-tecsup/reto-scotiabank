package com.scotiabank.challengue.domain.ports.input;

import com.scotiabank.challengue.application.dto.StudentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentUseCase {
    Mono<Void> createStudent(StudentDTO studentDTO);
    Flux<StudentDTO> getAllStudentsActive();
    Flux<StudentDTO> getAllStudents();
}
