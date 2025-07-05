package com.asle.quiz.domain;

public class QuizRequest {

    private String quizId;
    private String questionId;
    private String answer;

    public QuizRequest(String quizId, String questionId, String answer) {
        this.quizId = quizId;
        this.questionId = questionId;
        this.answer = answer;
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
