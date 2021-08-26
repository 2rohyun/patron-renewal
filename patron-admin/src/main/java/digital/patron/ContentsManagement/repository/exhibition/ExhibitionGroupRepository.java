package digital.patron.ContentsManagement.repository.exhibition;

import digital.patron.ContentsManagement.domain.exhibition.ExhibitionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionGroupRepository extends JpaRepository<ExhibitionGroup, Long> {
}
