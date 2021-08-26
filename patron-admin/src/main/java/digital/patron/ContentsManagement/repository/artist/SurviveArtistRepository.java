package digital.patron.ContentsManagement.repository.artist;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurviveArtistRepository extends JpaRepository<SurviveArtist, Long> {
    @Query("select s from SurviveArtist s where s.id = :sa_id ")
    Optional<SurviveArtist> findById(Long sa_id);

}
