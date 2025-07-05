package com.asle.quiz.domain;

import jakarta.validation.constraints.NotBlank;

public class UserSubmission {

    @NotBlank(message = "userId is mandatory")
    private String userId;
    @NotBlank(message = "quizId is mandatory")
    private String quizId;
    @NotBlank(message = "questionId is mandatory")
    private String questionId;
    @NotBlank(message = "answer is mandatory")
    private String answer;

    public UserSubmission(String userId, String quizId, String questionId, String answer) {
        this.userId = userId;
        this.quizId = quizId;
        this.questionId = questionId;
        this.answer = answer;
    }

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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
