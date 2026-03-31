package lk.ijse.minitoonseduhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProgressDTO {
    private Long childId;
    private String lessonName;
    private String category;
    private int starsEarned;
}