package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cartoons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cartoon {

    @Id
    @Column(name = "cartoon_id")
    private String cartoonId;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;
}