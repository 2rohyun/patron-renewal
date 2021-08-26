package digital.patron.ContentsManagement.domain.integrate;

import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "survive_artist_exhibition_relation")
@Getter
@Setter(AccessLevel.PRIVATE)
public class SurviveArtistExhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SurviveArtist surviveArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exhibition exhibition;

    public void setSurviveArtist(SurviveArtist surviveArtist) {
        this.surviveArtist = surviveArtist;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

}
