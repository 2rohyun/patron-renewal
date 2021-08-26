package digital.patron.ContentsManagement.domain.artist;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "death_artist_death_artist_tag_relation")
public class DeathArtistDeathArtistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeathArtist deathArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeathArtistTag deathArtistTag;

    public void setDeathArtist(DeathArtist deathArtist) {
        this.deathArtist = deathArtist;
    }

    public void setDeathArtistTag(DeathArtistTag deathArtistTag) {
        this.deathArtistTag = deathArtistTag;
    }

}
