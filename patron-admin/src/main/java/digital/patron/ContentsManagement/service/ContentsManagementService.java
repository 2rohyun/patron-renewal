package digital.patron.ContentsManagement.service;


import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.repository.artwork.ArtworkRepository;
import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import digital.patron.ContentsManagement.repository.artist.DeathArtistRepository;
import digital.patron.ContentsManagement.repository.artist.SurviveArtistRepository;
import digital.patron.ContentsManagement.repository.artwork.ArtworkRepository;
import digital.patron.ContentsManagement.repository.exhibition.ExhibitionRepository;
import digital.patron.PatronMembers.domain.GeneralMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentsManagementService {

    private final ArtworkRepository artworkRepository;
    private final DeathArtistRepository deathArtistRepository;
    private final SurviveArtistRepository surviveArtistRepository;
    private final ExhibitionRepository exhibitionRepository;

    //NOT APPROVED ARTWORK
    public List<Artwork> getNoApprovalArtworks(){
        return artworkRepository.findArtworkByApproveIsFalse();
    }

    public List<Artwork> searchNoApprovalArtworkByKeyword(String keyword) {
        return artworkRepository.findNoApprovalArtworksByKeyword(keyword);
    }

    //APPROVED ARTWORK

    public List<Artwork> getApprovedArtworks(){
        return artworkRepository.findArtworkByApproveIsTrue();
    }

    public List<Artwork> searchApprovedArtworkByKeyword(String keyword) {
        return artworkRepository.findApprovedArtworksByKeyword(keyword);
    }
    public Artwork findArtworkById(Long artworkId){
        return artworkRepository.findArtworkById(artworkId);
    }

    //ARTIST

    public List<DeathArtist> getDeathArtists(){
        return deathArtistRepository.findAll();
    }
    public List<SurviveArtist> getSurviveArtists(){
        return surviveArtistRepository.findAll();
    }


    public DeathArtist findDeathArtistById(Long da_id){
        return deathArtistRepository.findById(da_id).orElseThrow(() -> new IllegalArgumentException("Death Artist not found"));
    }
    public SurviveArtist findSurviveArtistById(Long sa_id){
        return surviveArtistRepository.findById(sa_id).orElseThrow(() -> new IllegalArgumentException("Survive Artist not found"));
    }


    //EXHIBITIONS

    public List<Exhibition> getExhibitions(){
        return exhibitionRepository.findAll();
    }


    public Exhibition getExhibitionById(Long exh_id){
        return exhibitionRepository.findById(exh_id).orElseThrow(()->new IllegalArgumentException("No Exhibition found"));
    }
}



