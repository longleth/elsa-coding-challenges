package com.asle.quiz.repository;

import com.asle.quiz.domain.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    UserScore findByUserIdAndQuizId(String userId, String quizId);
}
