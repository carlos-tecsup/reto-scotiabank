package com.scotiabank.challengue.application.exception;

public class BusinessException extends RuntimeException implements FieldError {
    private final String field;
    public BusinessException(String field, String message) {
        super(message);
        this.field = field;
    }

    @Override
    public String getFieldName() {
        return field;
    }
}
