package lk.ijse.minitoonseduhub.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonDto {
    private String cartoonId;
    private String title;
    private String category;
    private String videoUrl;
    private String ageGroup;
    private String status;
    private LocalDate createdAt;
}