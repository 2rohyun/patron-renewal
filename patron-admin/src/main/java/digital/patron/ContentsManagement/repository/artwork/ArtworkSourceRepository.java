package digital.patron.ContentsManagement.repository.artwork;

import digital.patron.ContentsManagement.domain.artwork.ArtworkSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkSourceRepository extends JpaRepository<ArtworkSource, Long> {
}
