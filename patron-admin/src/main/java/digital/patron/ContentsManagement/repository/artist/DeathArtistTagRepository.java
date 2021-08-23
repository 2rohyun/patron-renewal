package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.DeathArtistTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeathArtistTagRepository extends JpaRepository<DeathArtistTag, Long> {
}
