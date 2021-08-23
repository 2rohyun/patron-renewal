package digital.patron.ContentsManagement.service;


import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.repository.artwork.ArtworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentsManagementService {

    private final ArtworkRepository artworkRepository;

    public List<Artwork> getNoApprovalArtworks(){
        return artworkRepository.findArtworkByApproveIsFalse();
    }

}
