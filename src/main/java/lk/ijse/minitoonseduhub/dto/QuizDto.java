package lk.ijse.minitoonseduhub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDto {

    private String quizId;
    private String title;
    private String url;
    private String category;
    private String difficulty;
    private String status;
}