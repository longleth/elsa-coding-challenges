package com.asle.quiz.controller;

import com.asle.quiz.config.RequestBuffer;
import com.asle.quiz.domain.JoinRequest;
import com.asle.quiz.service.QuizService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class QuizControllerTest {

    private QuizController quizController;

    @Mock
    private QuizService quizService;

    @Mock
    private RequestBuffer requestBuffer;

    @BeforeEach
    public void setUp() {
        quizService = Mockito.mock(QuizService.class);
        requestBuffer = Mockito.mock(RequestBuffer.class);
        quizController = new QuizController(quizService, requestBuffer);
    }

    @Test
    public void testFilterItems() {

        JoinRequest joinRequest = new JoinRequest("Test", "Test");

        ResponseEntity<Void> responseEntity =
                quizController.joinQuiz(joinRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
