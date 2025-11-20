package com.scotiabank.challengue.infraestructure.adapters.validator;

import com.scotiabank.challengue.infraestructure.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T t) {
        if (t == null) {
            List<Map<String, String>> errors = List.of(
                    Map.of(
                            "field", "requestBody",
                            "message", "Request body no puede ser nulo"
                    )
            );
            return Mono.error(new ValidationException(errors));
        }

        Set<ConstraintViolation<T>> constraints = validator.validate(t);

        if (constraints == null || constraints.isEmpty()) {
            return Mono.just(t);
        }

        List<Map<String, String>> errors = constraints.stream()
                .map(constraintViolation -> Map.of(
                        "field", constraintViolation.getPropertyPath().toString(),
                        "message", constraintViolation.getMessage()
                ))
                .toList();

        return Mono.error(new ValidationException(errors));
    }
}
