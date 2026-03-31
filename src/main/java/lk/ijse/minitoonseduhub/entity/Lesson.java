package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String lessonId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String ageGroup;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String contentUrl;
}