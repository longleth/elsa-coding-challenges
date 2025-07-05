package com.asle.quiz.service;

import com.asle.quiz.config.QuizClient;
import com.asle.quiz.repository.UserScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class QuizServiceTest {

    private QuizService quizService;

    @Mock
    private UserScoreRepository userScoreRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private QuizClient quizClient;

    @BeforeEach
    public void setUp() throws Exception {
        userScoreRepository = Mockito.mock(UserScoreRepository.class);
        cacheService = Mockito.mock(CacheService.class);
        quizClient = Mockito.mock(QuizClient.class);
        quizService = new QuizService(userScoreRepository, cacheService, quizClient);
    }

    @Test
    public void testLeaderBoard() throws Exception {

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = new HashSet<>();

        when(cacheService.reverseRangeWithScores("leaderboard:quizId", 0, 10))
                .thenReturn(typedTuples);

        Set<ZSetOperations.TypedTuple<Object>> result = quizService.getLeaderboard("quizId");
        assertThat(result).isEqualTo(typedTuples);
    }
}
