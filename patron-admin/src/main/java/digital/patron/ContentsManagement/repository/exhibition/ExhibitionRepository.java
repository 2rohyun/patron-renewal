package digital.patron.ContentsManagement.repository.exhibition;

import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {


}
