package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.SurviveArtistTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurviveArtistTagRepository extends JpaRepository<SurviveArtistTag, Long> {
}
