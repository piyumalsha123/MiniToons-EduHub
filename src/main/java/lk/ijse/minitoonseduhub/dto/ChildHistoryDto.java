package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChildHistoryDto {

    private String lessonName;
    private String category;
    private int starsEarned;
    private LocalDateTime completedAt;

}