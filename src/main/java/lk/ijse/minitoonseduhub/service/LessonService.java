package lk.ijse.minitoonseduhub.service;

import jakarta.transaction.Transactional;
import lk.ijse.minitoonseduhub.dto.LessonDto;
import lk.ijse.minitoonseduhub.entity.Lesson;
import lk.ijse.minitoonseduhub.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private LessonDto toDto(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .lessonId(lesson.getLessonId())
                .title(lesson.getTitle())
                .category(lesson.getCategory())
                .difficulty(lesson.getDifficulty())
                .ageGroup(lesson.getAgeGroup())
                .createdDate(lesson.getCreatedDate())
                .status(lesson.getStatus())
                .contentUrl(lesson.getContentUrl())
                .build();
    }


    private String generateLessonId() {
        Long count = lessonRepository.count() + 1;
        return String.format("L%03d", count); // L001, L002, L003...
    }


    public LessonDto createLesson(LessonDto dto) {
        Lesson lesson = Lesson.builder()
                .lessonId(generateLessonId())
                .title(dto.getTitle())
                .category(dto.getCategory())
                .difficulty(dto.getDifficulty())
                .ageGroup(dto.getAgeGroup())
                .createdDate(dto.getCreatedDate() != null ? dto.getCreatedDate() : LocalDate.now())
                .status(dto.getStatus() != null ? dto.getStatus() : "Active")
                .contentUrl(dto.getContentUrl())
                .build();
        Lesson saved = lessonRepository.save(lesson);
        return toDto(saved);
    }


    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return toDto(lesson);
    }


    public LessonDto getLessonByLessonId(String lessonId) {
        Lesson lesson = lessonRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found for lessonId: " + lessonId));
        return toDto(lesson);
    }


    public LessonDto updateLessonByLessonId(String lessonId, LessonDto dto) {
        Lesson lesson = lessonRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found for lessonId: " + lessonId));
        lesson.setTitle(dto.getTitle());
        lesson.setCategory(dto.getCategory());
        lesson.setDifficulty(dto.getDifficulty());
        lesson.setAgeGroup(dto.getAgeGroup());
        lesson.setCreatedDate(dto.getCreatedDate() != null ? dto.getCreatedDate() : lesson.getCreatedDate());
        lesson.setStatus(dto.getStatus() != null ? dto.getStatus() : lesson.getStatus());
        lesson.setContentUrl(dto.getContentUrl());
        return toDto(lessonRepository.save(lesson));
    }


    @Transactional
    public void deleteLessonByLessonId(String lessonId) {
        lessonRepository.deleteByLessonId(lessonId);
    }
}