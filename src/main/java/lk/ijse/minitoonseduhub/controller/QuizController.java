package lk.ijse.minitoonseduhub.controller;

import lk.ijse.minitoonseduhub.dto.QuizDto;
import lk.ijse.minitoonseduhub.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
@CrossOrigin
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/save")
    public QuizDto createQuiz(@RequestBody QuizDto dto) {
        return quizService.createQuiz(dto);
    }

    @GetMapping
    public List<QuizDto> getAll() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public QuizDto getById(@PathVariable String id) {
        return quizService.getQuizById(id);
    }

    @PutMapping("/update/{id}")
    public QuizDto update(@PathVariable String id, @RequestBody QuizDto dto) {
        return quizService.updateQuiz(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        quizService.deleteQuiz(id);
    }
}