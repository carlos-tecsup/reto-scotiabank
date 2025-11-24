package com.scotiabank.challengue.infraestructure.adapters.output.persistence.redis.adapter;

import com.scotiabank.challengue.application.mapper.StudentMapper;
import com.scotiabank.challengue.domain.model.StudentModel;
import com.scotiabank.challengue.domain.ports.output.StudentRepositoryPort;
import com.scotiabank.challengue.infraestructure.adapters.output.persistence.redis.entity.StudentRedisEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@Qualifier("redisStudentRepository")
public class StudentRedisRepositoryAdapter implements StudentRepositoryPort {

    private static final String KEY_PREFIX = "student:";
    private static final String ALL_STUDENTS_KEY = "students:all";
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final StudentMapper mapper;

    public StudentRedisRepositoryAdapter(
            ReactiveRedisTemplate<String, Object> redisTemplate,
            StudentMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> save(StudentModel studentModel) {
        return Mono.defer(() -> {
            log.info("StudentRedisRepositoryAdapter - Saving student with ID: {} in Redis", studentModel.id());
            
            String key = KEY_PREFIX + studentModel.id();
            StudentRedisEntity entity = mapper.toRedisEntity(studentModel);

            return redisTemplate.opsForValue()
                    .set(key, entity, DEFAULT_TTL)
                    .flatMap(saved -> {
                        if (saved) {
                            log.info("StudentRedisRepositoryAdapter - Student saved successfully with ID: {}", studentModel.id());
                            return redisTemplate.opsForSet()
                                    .add(ALL_STUDENTS_KEY, studentModel.id().toString())
                                    .then();
                        } else {
                            log.error("StudentRedisRepositoryAdapter - Failed to save student with ID: {}", studentModel.id());
                            return Mono.error(new RuntimeException("Failed to save student in Redis"));
                        }
                    })
                    .doOnError(e -> log.error("StudentRedisRepositoryAdapter - Error saving student", e));
        });
    }

    @Override
    public Flux<StudentModel> searchStudents(String status) {
        log.info("StudentRedisRepositoryAdapter - Searching students with status: {}", status);
        
        return redisTemplate.opsForSet()
                .members(ALL_STUDENTS_KEY)
                .cast(String.class)
                .flatMap(id -> {
                    String key = KEY_PREFIX + id;
                    return redisTemplate.opsForValue()
                            .get(key)
                            .<StudentRedisEntity>handle((obj, sink) -> {
                                if (obj instanceof Map) {
                                    sink.next(mapToEntity((Map<?, ?>) obj));
                                } else if (obj instanceof StudentRedisEntity) {
                                    sink.next((StudentRedisEntity) obj);
                                } else {
                                    log.error("Unexpected type from Redis: {}", obj.getClass());
                                    sink.error(new RuntimeException("Unexpected Redis value type"));
                                }
                            })
                            .map(mapper::fromRedisEntity);
                })
                .filter(student -> Objects.isNull(status) || status.equals(student.status()))
                .sort((s1, s2) -> Long.compare(s2.id(), s1.id()))
                .doOnComplete(() -> log.info("StudentRedisRepositoryAdapter - Search completed"));
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        String key = KEY_PREFIX + id;
        log.info("StudentRedisRepositoryAdapter - Checking if student exists with ID: {}", id);
        
        return redisTemplate.hasKey(key)
                .doOnSuccess(exists -> log.info("StudentRedisRepositoryAdapter - Student exists: {}", exists));
    }

    private StudentRedisEntity mapToEntity(Map<?, ?> map) {
        return StudentRedisEntity.builder()
                .id(((Number) map.get("id")).longValue())
                .name((String) map.get("name"))
                .lastName((String) map.get("lastName"))
                .status((String) map.get("status"))
                .age(((Number) map.get("age")).intValue())
                .build();
    }
}
