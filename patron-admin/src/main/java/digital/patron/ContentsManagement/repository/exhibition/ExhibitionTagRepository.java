package digital.patron.ContentsManagement.repository.exhibition;

import digital.patron.ContentsManagement.domain.exhibition.ExhibitionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionTagRepository extends JpaRepository<ExhibitionTag, Long> {

}
