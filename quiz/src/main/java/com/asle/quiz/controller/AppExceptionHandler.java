package com.asle.quiz.controller;

import com.asle.quiz.exception.ParameterException;
import com.asle.quiz.exception.RequestHeaderException;
import com.asle.quiz.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ValidationException> handleRequestBodyValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ValidationException validationException = new ValidationException(errors);

        return new ResponseEntity<>(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ParameterException> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {

        Map<String, String> params = new HashMap<>();

        params.put("name", ex.getPropertyName());
        params.put("type", ex.getRequiredType().getName());
        params.put("value", ex.getValue().toString());

        ParameterException parameterException = new ParameterException(params);

        return new ResponseEntity<>(parameterException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { MissingRequestHeaderException.class })
    public ResponseEntity<RequestHeaderException> handleMissingRequestHeaderException(
            MissingRequestHeaderException ex) {

        Map<String, String> headers = new HashMap<>();

        headers.put("name", ex.getHeaderName());
        RequestHeaderException requestHeaderException = new RequestHeaderException(headers);

        return new ResponseEntity<>(requestHeaderException, HttpStatus.BAD_REQUEST);
    }
}
