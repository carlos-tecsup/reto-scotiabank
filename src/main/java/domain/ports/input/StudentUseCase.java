package domain.ports.input;

import com.scotiabank.challengue.application.dto.StudentDTO;
import domain.model.StudentModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentUseCase {
    Mono<Void> createStudent(StudentDTO studentDTO);
    Flux<StudentDTO> getAllStudentsActive();
    Flux<StudentDTO> getAllStudents();
}
