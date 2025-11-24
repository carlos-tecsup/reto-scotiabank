package com.scotiabank.challengue.domain.exceptions;


import com.scotiabank.challengue.application.exception.BusinessException;

public class DuplicatedStudentException extends BusinessException {

    private static final String FIELD = "id";

    public DuplicatedStudentException(String messageError) {
        super(FIELD, messageError);
    }
}