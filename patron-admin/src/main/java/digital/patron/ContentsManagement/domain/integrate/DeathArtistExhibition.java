package digital.patron.ContentsManagement.domain.integrate;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "death_artist_exhibition_relation")
@Getter
@Setter(AccessLevel.PRIVATE)
public class DeathArtistExhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeathArtist deathArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exhibition exhibition;

    public void setDeathArtist(DeathArtist deathArtist) {
        this.deathArtist = deathArtist;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

}
