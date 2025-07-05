package com.asle.quiz.performance;

import com.asle.quiz.QuizApplication;

import com.asle.quiz.domain.UserSubmission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.asle.quiz.utils.RequestTask;
import com.asle.quiz.utils.UserUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = QuizApplication.class)
public class PerformanceTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders headers;

    private String baseUrl;

    @BeforeEach
    public void setUp() {

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseUrl = "/api/quiz";
    }

    @Test
    public void givenUserIdShouldReturnTooManyRequestsWithinOneMinute() throws JsonProcessingException {

        // create another thread apart from main thread to execute performance test
        // will not impact by main thread which execute other test calls
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Submit 20 tasks to the main thread
        for (int i = 0; i < 10001; i++) {

            Runnable dummyTask = new RequestTask(testRestTemplate, headers, objectMapper, baseUrl, i);
            if (i < 10000) {
                executorService.execute(dummyTask);
            } else {
                int rejectedUser = i;
                executorService.execute(() -> {
                    // Sending 2001st request to get too many requests error
                    UserSubmission userSubmission = new UserSubmission(
                            Integer.toString(rejectedUser), "test", "test", "test");

                    HttpEntity<String> request = null;
                    try {
                        request = new HttpEntity<>(objectMapper.writeValueAsString(userSubmission), headers);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    ResponseEntity<Void> response2001st =
                            testRestTemplate.postForEntity(baseUrl + "/submit", request, Void.class);

                    assertThat(response2001st.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
                    assertThat(response2001st.getBody()).isNull();
                });
            }
        }
        executorService.shutdown();
    }
}
