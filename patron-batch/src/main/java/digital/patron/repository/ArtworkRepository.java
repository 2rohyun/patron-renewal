package digital.patron.repository;

import digital.patron.domain.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    @Query("select min(a.id) from Artwork a")
    long findMinId();

    @Query("select max(a.id) from Artwork a")
    long findMaxId();
}
