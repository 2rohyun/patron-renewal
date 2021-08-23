package digital.patron.ContentsManagement.domain.artist;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "survive_artist_survive_artist_tag_relation")
public class SurviveArtistSurviveArtistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SurviveArtist surviveArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private SurviveArtistTag surviveArtistTag;

    public void setSurviveArtist(SurviveArtist surviveArtist) {
        this.surviveArtist = surviveArtist;
    }

    public void setSurviveArtistTag(SurviveArtistTag surviveArtistTag) {
        this.surviveArtistTag = surviveArtistTag;
    }

}
