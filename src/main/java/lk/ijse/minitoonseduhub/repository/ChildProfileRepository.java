package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.ChildProfile;
import lk.ijse.minitoonseduhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {
    Optional<ChildProfile> findByUserId(Long userId);
    Optional<ChildProfile> findByUser(User user);
}