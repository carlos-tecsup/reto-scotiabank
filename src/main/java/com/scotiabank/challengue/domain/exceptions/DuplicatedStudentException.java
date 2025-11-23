package com.scotiabank.challengue.domain.exceptions;


public class DuplicatedStudentException extends RuntimeException {
    public DuplicatedStudentException(String messageError) {
        super(messageError);
    }
}