package com.asle.quiz.exception;

import java.util.Map;

public class ValidationException {

    private final Map<String, String> errors;
    private final String message;

    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
        this.message = "Request body validation failed";
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }
}
