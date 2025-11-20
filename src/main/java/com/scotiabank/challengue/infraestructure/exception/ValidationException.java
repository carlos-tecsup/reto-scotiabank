package com.scotiabank.challengue.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private final List<Map<String, String>> errors;

    public ValidationException(List<Map<String, String>> errors) {
        super("Validation error");
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}
