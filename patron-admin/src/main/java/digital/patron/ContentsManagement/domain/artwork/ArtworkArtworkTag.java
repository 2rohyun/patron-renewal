package digital.patron.ContentsManagement.domain.artwork;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "artwork_artwork_tag_relation")
public class ArtworkArtworkTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artwork artwork;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArtworkTag artworkTag;

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public void setArtworkTag(ArtworkTag artworkTag) {
        this.artworkTag = artworkTag;
    }
}
