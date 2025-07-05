package com.asle.quiz.api;

import com.asle.quiz.domain.JoinRequest;
import com.asle.quiz.domain.UserSubmission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.asle.quiz.QuizApplication;

import com.asle.quiz.exception.ParameterException;
import com.asle.quiz.exception.RequestHeaderException;
import com.asle.quiz.exception.ValidationException;
import org.json.JSONException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Set;

import static com.asle.quiz.utils.UserUtil.initTestUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = QuizApplication.class)
public class EndpointTest {

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
    public void givenUserJoinQuizReturnOk()
            throws JsonProcessingException, JSONException {

        JoinRequest userQuiz = new JoinRequest("user_1", "quiz_1");

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(userQuiz), headers);
        ResponseEntity<Void> response =
                testRestTemplate.postForEntity(baseUrl + "/join", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenInvalidJoinRequestReturnValidationErrorsJsonObject()
            throws JsonProcessingException, JSONException {

        JoinRequest joinRequest = new JoinRequest("", "");

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(joinRequest), headers);
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(baseUrl + "/join", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        ValidationException exception =
                objectMapper.readValue(response.getBody(), ValidationException.class);

        assertThat(exception.getMessage()).isEqualTo("Request body validation failed");
        assertThat(exception.getErrors().get("userId")).isEqualTo("userId is mandatory");
        assertThat(exception.getErrors().get("quizId")).isEqualTo("quizId is mandatory");
    }

    @Test
    public void givenUserQuizSubmissionReturnOk()
            throws JsonProcessingException, JSONException {

        UserSubmission userSubmission = new UserSubmission(
                "user_1", "quiz_1", "question_1", "answer_1");

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(userSubmission), headers);
        ResponseEntity<Void> response =
                testRestTemplate.postForEntity(baseUrl + "/submit", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenInvalidUserSubmissionReturnValidationErrorsJsonObject()
            throws JsonProcessingException, JSONException {

        UserSubmission userSubmission = new UserSubmission("", "", "", "");

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(userSubmission), headers);
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(baseUrl + "/submit", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        ValidationException exception =
                objectMapper.readValue(response.getBody(), ValidationException.class);

        assertThat(exception.getMessage()).isEqualTo("Request body validation failed");
        assertThat(exception.getErrors().get("userId")).isEqualTo("userId is mandatory");
        assertThat(exception.getErrors().get("quizId")).isEqualTo("quizId is mandatory");
        assertThat(exception.getErrors().get("questionId")).isEqualTo("questionId is mandatory");
        assertThat(exception.getErrors().get("answer")).isEqualTo("answer is mandatory");
    }

    @Test
    public void givenQuizIdShouldReturnCorrespondingLeaderBoard()
            throws JsonProcessingException, JSONException {

        initTestUser(baseUrl, headers, testRestTemplate, objectMapper);

        ResponseEntity<String> response =
                testRestTemplate.getForEntity(baseUrl + "/leaderboard/test", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
