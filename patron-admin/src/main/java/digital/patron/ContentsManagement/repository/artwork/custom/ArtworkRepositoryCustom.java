package digital.patron.ContentsManagement.repository.artwork.custom;

import digital.patron.ContentsManagement.domain.artwork.Artwork;

import java.util.List;

public interface ArtworkRepositoryCustom {
    List<Artwork> findNoApprovalArtworksByKeyword(String keyword);

    List<Artwork> findApprovedArtworksByKeyword(String keyword);

    Artwork findArtworkById(Long artworkId);

}
