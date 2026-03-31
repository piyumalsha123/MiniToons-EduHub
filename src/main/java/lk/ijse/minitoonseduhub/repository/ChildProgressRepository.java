package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.ChildProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildProgressRepository extends JpaRepository<ChildProgress, Long> {

    Optional<ChildProgress> findByChildIdAndLessonName(Long childId, String lessonName);
    List<ChildProgress> findAllByChildId(Long childId);
    List<ChildProgress> findByChildIdOrderByCompletedAtDesc(Long childId);
}