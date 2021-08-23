package digital.patron.ContentsManagement.repository.integrate;

import digital.patron.ContentsManagement.domain.integrate.DeathArtistExhibition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeathArtistExhibitionRepository extends JpaRepository<DeathArtistExhibition, Long> {


}
