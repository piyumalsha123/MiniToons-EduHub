package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    private String songId;

    private String title;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    private String ageGroup;
    private String status;

    private LocalDate createdAt;
}