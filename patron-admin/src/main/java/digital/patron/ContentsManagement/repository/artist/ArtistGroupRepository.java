package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.ArtistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistGroupRepository extends JpaRepository<ArtistGroup, Long> {
}
