package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.Role;
import lk.ijse.minitoonseduhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    long countByRole(Role role);
}