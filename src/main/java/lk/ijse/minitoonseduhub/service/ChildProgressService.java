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

    // 📌 1. Quiz එකක් ඉවර වුණාම Save හෝ Update කිරීම
    public String saveOrUpdateProgress(ChildProgress childProgress) {
        Optional<ChildProgress> existingProgress = childProgressRepository
                .findByChildIdAndLessonName(childProgress.getChildId(), childProgress.getLessonName());

        if (existingProgress.isPresent()) {
            ChildProgress current = existingProgress.get();

            // වැඩි තරු ගානක් අරන් නම් විතරක් Update කරනවා
            if (childProgress.getStarsEarned() > current.getStarsEarned()) {
                current.setStarsEarned(childProgress.getStarsEarned());
                current.setCompletedAt(LocalDateTime.now());
                current.setCompleted(true);
                current.setCategory(childProgress.getCategory()); // Category එකත් දාමු

                childProgressRepository.save(current);
                return "Progress updated with higher stars! ⭐";
            }
            return "Progress already exists with higher or equal stars.";

        } else {
            childProgress.setCompletedAt(LocalDateTime.now());
            childProgress.setCompleted(true);

            childProgressRepository.save(childProgress);
            return "New progress saved successfully! 🎉";
        }
    }

    // 📌 2. මුළු තරු ගණන ගැනීම
    public int getTotalStars(Long childId) {
        return childProgressRepository.findAllByChildId(childId).stream()
                .filter(ChildProgress::isCompleted) // Only completed
                .mapToInt(ChildProgress::getStarsEarned)
                .sum();
    }

    // 📌 3. ඉවර කරපු පාඩම් ගණන ගැනීම
    public int getTotalCompletedLessons(Long childId) {
        List<ChildProgress> progressList = childProgressRepository.findAllByChildId(childId);
        return (int) progressList.stream()
                .filter(ChildProgress::isCompleted)
                .map(ChildProgress::getLessonName)
                .distinct()
                .count();
    }

    // 📌 4. [මොඩිෆයි කරපු කොටස] Profile එකට Category අනුව Percentage එක නිවැරදිව හැදීම
    public List<CategoryProgressDTO> getProgressByCategory(Long childId) {
        List<ChildProgress> progressList = childProgressRepository.findAllByChildId(childId);

        // 🚀 අපේ අලුත් Quizzes වල උපරිම ලකුණු 10 නිසා මේක 10.0 විය යුතුයි!
        double maxStarsPerQuiz = 10.0;

        return progressList.stream()
                .filter(cp -> cp.getCategory() != null)
                // Category එක අනුව records ටික Group කරගන්නවා
                .collect(Collectors.groupingBy(ChildProgress::getCategory))
                .entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<ChildProgress> categoryQuizzes = entry.getValue();

                    double totalPercentage = 0;
                    int quizCount = categoryQuizzes.size();

                    for (ChildProgress p : categoryQuizzes) {
                        // එක Quiz එකකට අදාළ percentage එක මෙතනදී හදනවා
                        double quizPercentage = (p.getStarsEarned() / maxStarsPerQuiz) * 100;
                        totalPercentage += quizPercentage;
                    }

                    // 🚀 කරපු Quizzes ගණනෙන් බෙදලා Average එක ගන්නවා
                    int finalPercentage = 0;
                    if (quizCount > 0) {
                        finalPercentage = (int) Math.round(totalPercentage / quizCount);
                    }

                    // 100% ට වඩා වැඩි වෙන්න දෙන්නෙ නැහැ
                    if (finalPercentage > 100) finalPercentage = 100;

                    return CategoryProgressDTO.builder()
                            .category(categoryName)
                            .percentage(finalPercentage)
                            .build();
                })
                .collect(Collectors.toList());
    }
}