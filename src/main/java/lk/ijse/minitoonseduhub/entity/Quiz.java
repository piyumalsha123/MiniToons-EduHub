package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO INCREMENT

    @Column(nullable = false, unique = true)
    private String quizId; // Q001

    @Column(nullable = false)
    private String title; // Quiz name

    @Column(nullable = false)
    private String url; // quiz page (quiz.html)

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String status; // Active / Draft
}