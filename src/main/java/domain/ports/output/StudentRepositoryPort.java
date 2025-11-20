package domain.ports.output;

import com.scotiabank.challengue.infraestructure.output.persistence.entity.StudentEntity;
import domain.model.StudentModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepositoryPort {
    Mono<Void> save(StudentModel studentModel);
    Flux<StudentModel> findAllStudents();
    Mono<Boolean> existsById(Long id);

}
