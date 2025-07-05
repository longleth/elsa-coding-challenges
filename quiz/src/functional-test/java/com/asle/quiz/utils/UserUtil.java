package com.asle.quiz.utils;

import com.asle.quiz.domain.JoinRequest;
import com.asle.quiz.domain.UserSubmission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class UserUtil {

    /**
     * Invoke POST joining quiz and user submission to initialize test user to verify GET leader board endpoints
     */
    public static void initTestUser(String baseUrl, HttpHeaders headers,
                                    TestRestTemplate testRestTemplate,
                                    ObjectMapper objectMapper) throws JsonProcessingException {

        JoinRequest joinRequest = new JoinRequest("test", "test");

        HttpEntity<String> request1 =
                new HttpEntity<>(objectMapper.writeValueAsString(joinRequest), headers);
        ResponseEntity<String> response1 =
                testRestTemplate.postForEntity(baseUrl + "/join", request1, String.class);

        UserSubmission userSubmission = new UserSubmission(
                "test", "test", "test", "test");

        HttpEntity<String> request2 =
                new HttpEntity<>(objectMapper.writeValueAsString(userSubmission), headers);
        ResponseEntity<Void> response2 =
                testRestTemplate.postForEntity(baseUrl + "/submit", request2, Void.class);
    }
}
