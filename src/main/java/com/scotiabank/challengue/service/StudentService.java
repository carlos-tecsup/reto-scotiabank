package com.scotiabank.challengue.service;

import com.scotiabank.challengue.application.dto.StudentDTO;
import domain.port.input.StudentUseCase;
import domain.port.out.StudentRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService implements StudentUseCase {
    private final StudentRepositoryPort studentRepositoryPort;

    public StudentService(StudentRepositoryPort studentRepositoryPort) {
        this.studentRepositoryPort = studentRepositoryPort;
    }

    @Override
    public Mono<Void> createStudent(StudentDTO studentDTO) {
        return null;
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
