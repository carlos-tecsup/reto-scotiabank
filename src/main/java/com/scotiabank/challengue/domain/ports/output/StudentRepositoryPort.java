package com.scotiabank.challengue.domain.ports.output;

import com.scotiabank.challengue.domain.model.StudentModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepositoryPort {
    Mono<Void> save(StudentModel studentModel);
    Flux<StudentModel> searchStudents(String status);
    Mono<Boolean> existsById(Long id);
}
