package digital.patron.ContentsManagement.repository.exhibition;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    @Query("select e from Exhibition e where e.id = :exh_id")
    Optional<Exhibition> findById(Long exh_id);

}
