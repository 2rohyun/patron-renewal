package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.SurviveArtistArtistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurviveArtistArtistGroupRepository extends JpaRepository<SurviveArtistArtistGroup, Long> {
}
