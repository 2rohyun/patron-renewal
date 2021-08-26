package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeathArtistRepository extends JpaRepository<DeathArtist, Long> {
    @Query("select d from DeathArtist d where d.id = :da_id ")
    Optional<DeathArtist> findById(Long da_id);

}
