package com.scotiabank.challengue.infraestructure.config;

import com.scotiabank.challengue.infraestructure.adapters.output.persistence.redis.entity.StudentRedisEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Inicializa datos de prueba en Redis al iniciar la aplicación.
 */
@Configuration
@Slf4j
public class RedisDataInitializer {

    private static final String KEY_PREFIX = "student:";
    private static final String ALL_STUDENTS_KEY = "students:all";
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    @Bean
    public CommandLineRunner initRedisData(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return args -> {
            log.info("Verificando si Redis necesita inicialización...");

            redisTemplate.hasKey(ALL_STUDENTS_KEY)
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            log.info("Redis ya contiene datos. Omitiendo inicialización.");
                            return Mono.empty();
                        }

                        log.info("Inicializando datos de prueba en Redis...");
                        
                        List<StudentRedisEntity> students = List.of(
                                StudentRedisEntity.builder().id(1L).name("Roberto").lastName("Gomez").status("1").age(26).build(),
                                StudentRedisEntity.builder().id(2L).name("Valentina").lastName("Torres").status("1").age(23).build(),
                                StudentRedisEntity.builder().id(3L).name("Sebastian").lastName("Ramirez").status("1").age(21).build(),
                                StudentRedisEntity.builder().id(4L).name("Isabella").lastName("Castro").status("1").age(27).build(),
                                StudentRedisEntity.builder().id(5L).name("Fernando").lastName("Morales").status("0").age(25).build(),
                                StudentRedisEntity.builder().id(6L).name("Camila").lastName("Herrera").status("1").age(22).build(),
                                StudentRedisEntity.builder().id(7L).name("Diego").lastName("Jimenez").status("1").age(29).build(),
                                StudentRedisEntity.builder().id(8L).name("Lucia").lastName("Vargas").status("0").age(24).build(),
                                StudentRedisEntity.builder().id(9L).name("Andres").lastName("Ortiz").status("1").age(28).build(),
                                StudentRedisEntity.builder().id(10L).name("Gabriela").lastName("Rojas").status("1").age(20).build()
                        );

                        return Flux.fromIterable(students)
                                .flatMap(student -> {
                                    String key = KEY_PREFIX + student.getId();
                                    return redisTemplate.opsForValue()
                                            .set(key, student, DEFAULT_TTL)
                                            .flatMap(saved -> redisTemplate.opsForSet()
                                                    .add(ALL_STUDENTS_KEY, student.getId().toString())
                                                    .then());
                                })
                                .then(Mono.fromRunnable(() -> 
                                    log.info("✓ Datos de prueba inicializados en Redis: 10 estudiantes (8 activos, 2 inactivos)")
                                ));
                    })
                    .doOnError(e -> log.error("Error al inicializar datos en Redis", e))
                    .subscribe();
        };
    }
}
