package lk.ijse.minitoonseduhub.service;

import lk.ijse.minitoonseduhub.dto.QuizDto;
import lk.ijse.minitoonseduhub.entity.Quiz;
import lk.ijse.minitoonseduhub.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    private QuizDto toDto(Quiz quiz) {
        return QuizDto.builder()
                .quizId(quiz.getQuizId())
                .title(quiz.getTitle())
                .url(quiz.getUrl())
                .category(quiz.getCategory())
                .difficulty(quiz.getDifficulty())
                .status(quiz.getStatus())
                .build();
    }

    private String generateQuizId() {
        long count = quizRepository.count() + 1;
        return String.format("Q%03d", count);
    }


    public QuizDto createQuiz(QuizDto dto) {

        Quiz quiz = Quiz.builder()
                .quizId(generateQuizId())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .category(dto.getCategory())
                .difficulty(dto.getDifficulty() != null ? dto.getDifficulty() : "Easy")
                .status(dto.getStatus() != null ? dto.getStatus() : "Active")
                .build();

        return toDto(quizRepository.save(quiz));
    }


    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public QuizDto getQuizById(String quizId) {
        Quiz quiz = quizRepository.findByQuizId(quizId);

        if (quiz == null) {
            throw new RuntimeException("Quiz not found: " + quizId);
        }

        return toDto(quiz);
    }

    public QuizDto updateQuiz(String quizId, QuizDto dto) {

        Quiz quiz = quizRepository.findByQuizId(quizId);

        if (quiz == null) {
            throw new RuntimeException("Quiz not found: " + quizId);
        }

        quiz.setTitle(dto.getTitle());
        quiz.setUrl(dto.getUrl());
        quiz.setCategory(dto.getCategory());
        quiz.setDifficulty(dto.getDifficulty());
        quiz.setStatus(dto.getStatus());

        return toDto(quizRepository.save(quiz));
    }


    public void deleteQuiz(String quizId) {

        Quiz quiz = quizRepository.findByQuizId(quizId);

        if (quiz == null) {
            throw new RuntimeException("Quiz not found: " + quizId);
        }

        quizRepository.delete(quiz);
    }
}