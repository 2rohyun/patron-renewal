package digital.patron.ContentsManagement.repository.artwork;

import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.repository.artwork.custom.ArtworkRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> , ArtworkRepositoryCustom {

    @Query("select a from Artwork a where a.approve = false ")
    List<Artwork> findArtworkByApproveIsFalse();

    @Query("select a from Artwork a where a.approve = true ")
    List<Artwork> findArtworkByApproveIsTrue();
}
