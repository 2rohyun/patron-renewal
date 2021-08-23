package digital.patron.ContentsManagement.repository.artwork;

import digital.patron.ContentsManagement.domain.artwork.ContentsThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsThumbnailRepository extends JpaRepository<ContentsThumbnail, Long> {
}
