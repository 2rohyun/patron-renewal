package digital.patron.ContentsManagement.domain.artist;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "survive_artist_artist_group_relation")
public class SurviveArtistArtistGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SurviveArtist surviveArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArtistGroup artistGroup;

    public void setSurviveArtist(SurviveArtist surviveArtist) {
        this.surviveArtist = surviveArtist;
    }

    public void setArtistGroup(ArtistGroup artistGroup) {
        this.artistGroup = artistGroup;
    }
}
