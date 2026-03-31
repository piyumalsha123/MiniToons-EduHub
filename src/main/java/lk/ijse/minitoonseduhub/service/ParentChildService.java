package lk.ijse.minitoonseduhub.service;

import lk.ijse.minitoonseduhub.dto.ChildHistoryDto;
import lk.ijse.minitoonseduhub.dto.ChildProgressDTO;
import lk.ijse.minitoonseduhub.entity.ChildProfile;
import lk.ijse.minitoonseduhub.entity.ChildProgress;
import lk.ijse.minitoonseduhub.entity.ParentChild;
import lk.ijse.minitoonseduhub.entity.User;
import lk.ijse.minitoonseduhub.repository.ChildProgressRepository;
import lk.ijse.minitoonseduhub.repository.LessonRepository;
import lk.ijse.minitoonseduhub.repository.ParentChildRepository;
import lk.ijse.minitoonseduhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentChildService {

    private final ParentChildRepository parentChildRepository;
    private final UserRepository userRepository;
    private final ChildProgressRepository childProgressRepository;
    private final LessonRepository lessonRepository;

    public List<ChildProfile> getChildrenByParentUsername(String username) {
        User parent = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Parent not found!"));

        return parentChildRepository.findByParent(parent)
                .stream()
                .map(ParentChild::getChild)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getInsights(Long childId) {
        List<ChildProgress> list = childProgressRepository.findAllByChildId(childId);


        int totalLessons = (int) lessonRepository.count();


        int completedLessons = (int) list.stream().map(ChildProgress::getLessonName).distinct().count();

        List<ChildProgress> completedList = list.stream().filter(ChildProgress::isCompleted).toList();
        int totalStars = completedList.stream().mapToInt(ChildProgress::getStarsEarned).sum();
        int avgScore = completedLessons == 0 ? 0 : (int)Math.round((double)totalStars / completedLessons);


        Map<String, Long> categoryMap = completedList.stream()
                .collect(Collectors.groupingBy(ChildProgress::getCategory, Collectors.counting()));

        List<Map<String, Object>> categories = new ArrayList<>();
        for (Map.Entry<String, Long> e : categoryMap.entrySet()) {
            Map<String, Object> m = new HashMap<>();
            m.put("category", e.getKey());
            m.put("count", e.getValue());
            categories.add(m);
        }

        List<Map<String, Object>> history = new ArrayList<>();
        for (ChildProgress p : completedList) {
            Map<String, Object> m = new HashMap<>();
            m.put("lessonName", p.getLessonName());
            m.put("category", p.getCategory());
            m.put("starsEarned", p.getStarsEarned());
            m.put("completedAt", p.getCompletedAt());
            history.add(m);
        }
        Map<String,Object> response = new HashMap<>();
        response.put("totalLessons", totalLessons);
        response.put("completedLessons", completedLessons);
        response.put("totalStars", totalStars);
        response.put("avgScore", avgScore);
        response.put("categories", categories);
        response.put("history", history);

        return response;
    }

//    public List<ChildProgressDTO> getChildProgress(Long childId) {
//        return childProgressRepository.findAllByChildId(childId).stream()
//                .map(p -> new ChildProgressDTO(p.getChildId(), p.getLessonName(), p.getCategory(), p.getStarsEarned()))
//                .collect(Collectors.toList());
//    }

    public List<ChildHistoryDto> getChildHistory(Long childId) {
        return childProgressRepository.findByChildIdOrderByCompletedAtDesc(childId).stream()
                .map(p -> new ChildHistoryDto(p.getLessonName(), p.getCategory(), p.getStarsEarned(), p.getCompletedAt()))
                .toList();
    }
}