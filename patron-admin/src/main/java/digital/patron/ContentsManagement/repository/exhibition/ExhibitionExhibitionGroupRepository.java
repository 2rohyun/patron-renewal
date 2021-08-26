package digital.patron.ContentsManagement.repository.exhibition;

import digital.patron.ContentsManagement.domain.exhibition.ExhibitionExhibitionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionExhibitionGroupRepository extends JpaRepository<ExhibitionExhibitionGroup, Long> {
}
