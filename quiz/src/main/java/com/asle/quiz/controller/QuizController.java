package com.asle.quiz.controller;

import com.asle.quiz.config.RequestBuffer;
import com.asle.quiz.domain.JoinRequest;
import com.asle.quiz.domain.UserSubmission;
import com.asle.quiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "http://localhost:5173")
public class QuizController {

    private final QuizService quizService;

    private final RequestBuffer buffer;

    @Autowired
    public QuizController(QuizService quizService, RequestBuffer buffer) {
        this.quizService = quizService;
        this.buffer = buffer;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinQuiz(@Valid @RequestBody JoinRequest request) {

        String quizId = request.getQuizId();
        String userId = request.getUserId();
        quizService.joinQuiz(quizId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@Valid @RequestBody UserSubmission request) {

        boolean accepted = buffer.offer(request);
        if (accepted) {
            // quizService.submitAnswer(quizId, userId, questionId, answer);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping("/leaderboard/{quizId}")
    public ResponseEntity<Set<ZSetOperations.TypedTuple<Object>>> getLeaderboard(
            @PathVariable String quizId) {

        Set<ZSetOperations.TypedTuple<Object>> leaderboard = quizService.getLeaderboard(quizId);
        return ResponseEntity.status(HttpStatus.OK).body(leaderboard);
    }
}
