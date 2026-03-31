package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Quiz findByQuizId(String quizId);
}