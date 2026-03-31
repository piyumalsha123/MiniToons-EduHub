package lk.ijse.minitoonseduhub.service;

import lk.ijse.minitoonseduhub.dto.CategoryProgressDTO;
import lk.ijse.minitoonseduhub.entity.ChildProgress;
import lk.ijse.minitoonseduhub.repository.ChildProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChildProgressService {

    private final ChildProgressRepository childProgressRepository;


    public String saveOrUpdateProgress(ChildProgress childProgress) {
        Optional<ChildProgress> existingProgress = childProgressRepository
                .findByChildIdAndLessonName(childProgress.getChildId(), childProgress.getLessonName());

        if (existingProgress.isPresent()) {
            ChildProgress current = existingProgress.get();


            if (childProgress.getStarsEarned() > current.getStarsEarned()) {
                current.setStarsEarned(childProgress.getStarsEarned());
                current.setCompletedAt(LocalDateTime.now());
                current.setCompleted(true);
                current.setCategory(childProgress.getCategory());
                childProgressRepository.save(current);
                return "Progress updated with higher stars! ";
            }
            return "Progress already exists with higher or equal stars.";

        } else {
            childProgress.setCompletedAt(LocalDateTime.now());
            childProgress.setCompleted(true);

            childProgressRepository.save(childProgress);
            return "New progress saved successfully! ";
        }
    }


    public int getTotalStars(Long childId) {
        return childProgressRepository.findAllByChildId(childId).stream()
                .filter(ChildProgress::isCompleted) // Only completed
                .mapToInt(ChildProgress::getStarsEarned)
                .sum();
    }


    public int getTotalCompletedLessons(Long childId) {
        List<ChildProgress> progressList = childProgressRepository.findAllByChildId(childId);
        return (int) progressList.stream()
                .filter(ChildProgress::isCompleted)
                .map(ChildProgress::getLessonName)
                .distinct()
                .count();
    }

    public List<CategoryProgressDTO> getProgressByCategory(Long childId) {
        List<ChildProgress> progressList = childProgressRepository.findAllByChildId(childId);


        double maxStarsPerQuiz = 10.0;

        return progressList.stream()
                .filter(cp -> cp.getCategory() != null)

                .collect(Collectors.groupingBy(ChildProgress::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<ChildProgress> categoryQuizzes = entry.getValue();

                    double totalPercentage = 0;
                    int quizCount = categoryQuizzes.size();

                    for (ChildProgress p : categoryQuizzes) {

                        double quizPercentage = (p.getStarsEarned() / maxStarsPerQuiz) * 100;
                        totalPercentage += quizPercentage;
                    }


                    int finalPercentage = 0;
                    if (quizCount > 0) {
                        finalPercentage = (int) Math.round(totalPercentage / quizCount);
                    }


                    if (finalPercentage > 100) finalPercentage = 100;

                    return CategoryProgressDTO.builder()
                            .category(categoryName)
                            .percentage(finalPercentage)
                            .build();
                })
                .collect(Collectors.toList());
    }
}