package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "child_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}