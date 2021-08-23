package digital.patron.ContentsManagement.domain.artwork;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class ArtworkTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000, name = "name")
    private String tagName;

    @OneToMany(mappedBy = "artworkTag")
    private Set<ArtworkArtworkTag> artworkArtworkTags = new HashSet<>();

    protected ArtworkTag() {
    }

    public ArtworkTag(String tagName) {
        this.tagName = tagName;
    }

    public void addArtworkArtworkTag(ArtworkArtworkTag artworkArtworkTag) {
        artworkArtworkTags.add(artworkArtworkTag);
        artworkArtworkTag.setArtworkTag(this);
    }
}
