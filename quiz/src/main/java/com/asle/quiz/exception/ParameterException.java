package com.asle.quiz.exception;

import java.util.Map;

public class ParameterException {

    private final Map<String, String> parameters;
    private final String message;

    public ParameterException(Map<String, String> parameters) {
        this.parameters = parameters;
        this.message = "Parameter validation failed";
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getMessage() {
        return message;
    }
}
