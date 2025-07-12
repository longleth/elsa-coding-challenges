package com.asle.quiz.config;

import com.asle.quiz.domain.QuizRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class QuizClient {

    private final WebClient webClient;

    public QuizClient() {
        this.webClient = WebClient.builder()
                //.baseUrl("http://localhost:8081/api/quiz")
                .baseUrl("http://elsa-quiz:8081/api/quiz")
                .build();
    }

    public Boolean validateQuiz(String quizId, String questionId, String answer) {

        QuizRequest quizRequest = new QuizRequest(quizId, questionId, answer);
        return webClient.post()
                .uri("/validate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(quizRequest)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .block();
    }
}
