package digital.patron.ContentsManagement.repository.artwork;

import digital.patron.ContentsManagement.domain.artwork.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Long> {
}
