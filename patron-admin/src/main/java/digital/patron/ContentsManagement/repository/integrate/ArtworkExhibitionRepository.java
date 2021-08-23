package digital.patron.ContentsManagement.repository.integrate;

import digital.patron.ContentsManagement.domain.integrate.ArtworkExhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkExhibitionRepository extends JpaRepository<ArtworkExhibition, Long> {
}
