package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.CategoryProgressDTO;
import lk.ijse.minitoonseduhub.dto.ChildProgressDTO;
import lk.ijse.minitoonseduhub.entity.ChildProgress;
import lk.ijse.minitoonseduhub.service.ChildProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@CrossOrigin (origins = "http://localhost:63342")
public class ChildProgressController {

    private final ChildProgressService childProgressService;

    @PostMapping("/save")
    public ResponseEntity<String> saveProgress(@RequestBody ChildProgressDTO dto) {


        ChildProgress childProgress = ChildProgress.builder()
                .childId(dto.getChildId())
                .lessonName(dto.getLessonName())
                .category(dto.getCategory())
                .starsEarned(dto.getStarsEarned())
                .build();

        String message = childProgressService.saveOrUpdateProgress(childProgress);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/total-stars/{childId}")
    public ResponseEntity<Integer> getTotalStars(@PathVariable Long childId) {
        int totalStars = childProgressService.getTotalStars(childId);
        return ResponseEntity.ok(totalStars);
    }

    @GetMapping("/total-lessons/{childId}")
    public ResponseEntity<Integer> getTotalLessons(@PathVariable Long childId) {
        int totalLessons = childProgressService.getTotalCompletedLessons(childId);
        return ResponseEntity.ok(totalLessons);
    }

    @GetMapping("/category-progress/{childId}")
    public ResponseEntity<List<CategoryProgressDTO>> getCategoryProgress(@PathVariable Long childId) {
        List<CategoryProgressDTO> progressList = childProgressService.getProgressByCategory(childId);
        return ResponseEntity.ok(progressList);
    }
}