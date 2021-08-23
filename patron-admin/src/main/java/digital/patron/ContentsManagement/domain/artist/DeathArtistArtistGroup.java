package digital.patron.ContentsManagement.domain.artist;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "death_artist_artist_group_relation")
public class DeathArtistArtistGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeathArtist deathArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArtistGroup artistGroup;

    public void setDeathArtist(DeathArtist deathArtist) {
        this.deathArtist = deathArtist;
    }

    public void setArtistGroup(ArtistGroup artistGroup) {
        this.artistGroup = artistGroup;
    }
}
