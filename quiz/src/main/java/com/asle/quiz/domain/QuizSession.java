package com.asle.quiz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class QuizSession {

    @Id
    private String quizId;
    private String status;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
