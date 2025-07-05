package com.asle.quiz.service;

import com.asle.quiz.config.RequestBuffer;
import com.asle.quiz.domain.UserSubmission;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class RequestProcessor {

    private final RequestBuffer buffer;
    private final QuizService quizService;

    @Autowired
    public RequestProcessor(RequestBuffer buffer, QuizService quizService) {
        this.buffer = buffer;
        this.quizService = quizService;
    }

    @PostConstruct
    public void startConsumer() {

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            UserSubmission request;
            while ((request = buffer.poll()) != null) {
                process(request);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    private void process(UserSubmission request) {

        System.out.println("Updating user score: " + request);
        // business logic
        quizService.submitAnswer(request);
    }
}
