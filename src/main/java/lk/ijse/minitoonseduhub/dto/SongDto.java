package lk.ijse.minitoonseduhub.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDto {

    private String songId;

    private String title;
    private String videoUrl;
    private String lyrics;
    private String ageGroup;
    private String status;
    private LocalDate createdAt;
}
