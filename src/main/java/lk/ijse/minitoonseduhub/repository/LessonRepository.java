package lk.ijse.minitoonseduhub.repository;

import jakarta.transaction.Transactional;
import lk.ijse.minitoonseduhub.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByLessonId(String lessonId);

    @Transactional
    void deleteByLessonId(String lessonId);
}