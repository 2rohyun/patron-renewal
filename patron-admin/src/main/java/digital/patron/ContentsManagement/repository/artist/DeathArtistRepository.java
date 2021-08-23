package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeathArtistRepository extends JpaRepository<DeathArtist, Long> {
}
