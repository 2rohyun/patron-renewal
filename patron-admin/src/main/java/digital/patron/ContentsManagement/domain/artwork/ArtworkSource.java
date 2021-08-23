package digital.patron.ContentsManagement.domain.artwork;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class ArtworkSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Long id;

    @Column(nullable = false, length = 100, name = "name")
    private String sourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artwork artwork;

    protected ArtworkSource() {
    }

    public ArtworkSource(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
