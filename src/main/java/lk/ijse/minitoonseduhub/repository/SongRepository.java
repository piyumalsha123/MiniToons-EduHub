package lk.ijse.minitoonseduhub.repository;

import lk.ijse.minitoonseduhub.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SongRepository extends JpaRepository<Song, String> {
}
