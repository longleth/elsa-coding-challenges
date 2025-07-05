package com.asle.quiz.utils;

import com.asle.quiz.domain.UserSubmission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class RequestTask implements Runnable {

    private final TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final HttpHeaders headers;

    private final String baseUrl;

    private final int requestOrder;

    public RequestTask(TestRestTemplate restTemplate, HttpHeaders headers,
                       ObjectMapper objectMapper, String baseUrl, int requestOrder) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.requestOrder = requestOrder;
    }

    @Override
    public void run() {

        try {

            System.out.println("Request " + requestOrder + " is running on thread " +
                    Thread.currentThread().getName());

            UserSubmission userSubmission = new UserSubmission(
                    Integer.toString(requestOrder), "test", "test", "test");

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(userSubmission), headers);
            ResponseEntity<Void> response =
                    restTemplate.postForEntity(baseUrl + "/submit", request, Void.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
