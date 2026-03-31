package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @Column(name = "child_id", nullable = false)
    private Long childId;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "category")
    private String category;

    @Column(name = "stars_earned", nullable = false)
    private int starsEarned;

    @Column(name = "is_completed", nullable = false)
    private boolean completed; // ⭐ මෙතන completed කියලා දැම්මම ලේසියි

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}