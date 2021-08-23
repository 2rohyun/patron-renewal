package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurviveArtistRepository extends JpaRepository<SurviveArtist, Long> {
}
