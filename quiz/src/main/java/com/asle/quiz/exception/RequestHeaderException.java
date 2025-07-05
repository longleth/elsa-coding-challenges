package com.asle.quiz.exception;

import java.util.Map;

public class RequestHeaderException {

    private final Map<String, String> headers;
    private final String message;

    public RequestHeaderException(Map<String, String> headers) {
        this.headers = headers;
        this.message = "Missing request header";
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMessage() {
        return message;
    }
}
