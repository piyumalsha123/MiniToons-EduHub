package lk.ijse.minitoonseduhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parent_child")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentChild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @OneToOne
    @JoinColumn(name = "child_id")
    private ChildProfile child;
}