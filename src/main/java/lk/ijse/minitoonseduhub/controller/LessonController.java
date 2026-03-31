package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.ApiResponse;
import lk.ijse.minitoonseduhub.dto.LessonDto;
import lk.ijse.minitoonseduhub.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDto> getLessonByLessonId(@PathVariable String lessonId) {
        return ResponseEntity.ok(lessonService.getLessonByLessonId(lessonId));
    }

    @PostMapping("/save")
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto dto) {
        return ResponseEntity.ok(lessonService.createLesson(dto));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable String lessonId, @RequestBody LessonDto dto) {
        return ResponseEntity.ok(lessonService.updateLessonByLessonId(lessonId, dto));
    }

    @DeleteMapping("/delete/{lessonId}")
    public ResponseEntity<ApiResponse> deleteLesson(@PathVariable String lessonId) {
        lessonService.deleteLessonByLessonId(lessonId);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "Lesson deleted successfully",
                null
        ));
    }
}