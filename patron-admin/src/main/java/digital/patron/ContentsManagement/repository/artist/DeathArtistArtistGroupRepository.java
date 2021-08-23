package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.DeathArtistArtistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeathArtistArtistGroupRepository extends JpaRepository<DeathArtistArtistGroup, Long> {
}
