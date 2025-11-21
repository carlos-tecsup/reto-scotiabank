package com.scotiabank.challengue.domain.ports.input;

import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;
import reactor.core.publisher.Mono;

public interface StudentUseCase {
    Mono<Void> createStudent(CreateStudentRequestDTO createStudentRequestDTO);
    Mono<SearchStudentResponseDTO> searchStudents(SearchStudentsRequestDTO searchRequest);
}
