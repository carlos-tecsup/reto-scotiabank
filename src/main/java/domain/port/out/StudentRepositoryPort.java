package domain.port.out;

import com.scotiabank.challengue.infraestructure.output.persistence.entity.StudentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepositoryPort {
    Mono<Void> createStudent(StudentEntity studentEntity);
    Flux<StudentEntity> findAllStudents();
}
