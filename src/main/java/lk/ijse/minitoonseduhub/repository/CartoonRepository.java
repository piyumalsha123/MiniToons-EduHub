package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.Cartoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartoonRepository extends JpaRepository<Cartoon, String> {
}