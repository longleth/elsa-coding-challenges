package com.asle.quiz.domain;

import jakarta.validation.constraints.NotBlank;

public class JoinRequest {

    @NotBlank(message = "userId is mandatory")
    private String userId;
    @NotBlank(message = "quizId is mandatory")
    private String quizId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
