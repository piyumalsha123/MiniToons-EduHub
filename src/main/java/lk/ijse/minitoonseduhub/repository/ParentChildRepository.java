package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.ParentChild;
import lk.ijse.minitoonseduhub.entity.User;
import lk.ijse.minitoonseduhub.entity.ChildProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentChildRepository extends JpaRepository<ParentChild, Long> {

    List<ParentChild> findByParent(User parent);

    void deleteByChild(ChildProfile child);

    boolean existsByParentAndChild(User parent, ChildProfile child);

    void deleteByChildId(Long childId);
}