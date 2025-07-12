package com.asle.quiz.service;

import com.asle.quiz.config.QuizClient;
import com.asle.quiz.domain.UserScore;
import com.asle.quiz.domain.UserSubmission;
import com.asle.quiz.repository.UserScoreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QuizService {

    private static final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final UserScoreRepository userScoreRepository;

    private final CacheService cacheService;

    private final QuizClient quizClient;

    @Autowired
    public QuizService(UserScoreRepository userScoreRepository, CacheService cacheService, QuizClient quizClient) {
        this.userScoreRepository = userScoreRepository;
        this.cacheService = cacheService;
        this.quizClient = quizClient;
    }

    public void joinQuiz(String quizId, String userId) {

        log.info("User [{}] joined quiz [{}]", userId, quizId);
        // Logic to add user to quiz session
        cacheService.add("quiz:" + quizId + ":participants", userId);
    }

    public void submitAnswer(UserSubmission submission) {

        boolean isCorrect = quizClient.validateQuiz(
                submission.getQuizId(),
                submission.getQuestionId(),
                submission.getAnswer());

        log.info("User submission [quizId: {}, questionId: {}, answer: {}]",
                submission.getQuizId(), submission.getQuestionId(), submission.getAnswer());

        if (isCorrect) {
            updateScore(submission.getUserId(), submission.getQuizId());
        }
    }

    private void updateScore(String userId, String quizId) {

        UserScore userScore = userScoreRepository.findByUserIdAndQuizId(userId, quizId);

        if (userScore == null) {

            userScore = new UserScore();
            userScore.setUserId(userId);
            userScore.setQuizId(quizId);
            userScore.setScore(0);
        }

        userScore.setScore(userScore.getScore() + 1);
        userScoreRepository.save(userScore);

        log.info("Update user score [{}] of quiz [{}]: -> score [{}]", userId, quizId, userScore.getScore());

        cacheService.incrementScore("leaderboard:" + quizId, userId, 1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getLeaderboard(String quizId) {

        log.info("Loading leader board of quiz [{}]", quizId);
        return cacheService.reverseRangeWithScores("leaderboard:" + quizId, 0, 10);
    }
}
