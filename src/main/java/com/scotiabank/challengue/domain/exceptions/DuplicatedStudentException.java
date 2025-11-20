package com.scotiabank.challengue.domain.exceptions;

public class DuplicatedStudentException extends RuntimeException {
    public DuplicatedStudentException(Long id) {
        super("El estudiante con ID " + id + " ya existe");
    }
}