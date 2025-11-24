package com.scotiabank.challengue.infraestructure.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    private final List<Map<String, String>> errors;

}
