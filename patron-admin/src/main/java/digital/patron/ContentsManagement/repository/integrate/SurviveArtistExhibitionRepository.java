package digital.patron.ContentsManagement.repository.integrate;

import digital.patron.ContentsManagement.domain.integrate.SurviveArtistExhibition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurviveArtistExhibitionRepository extends JpaRepository<SurviveArtistExhibition, Long> {

}
