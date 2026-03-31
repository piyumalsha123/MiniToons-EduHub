package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {

    private Long id;
    private String lessonId;
    private String title;
    private String category;
    private String difficulty;
    private String ageGroup;
    private LocalDate createdDate;
    private String status;
    private String contentUrl;
}