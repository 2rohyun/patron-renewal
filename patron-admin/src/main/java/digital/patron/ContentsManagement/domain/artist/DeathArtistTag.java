package digital.patron.ContentsManagement.domain.artist;

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
public class DeathArtistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000, name = "name")
    private String tagName;

    @OneToMany(mappedBy = "deathArtistTag")
    private Set<DeathArtistDeathArtistTag> deathArtistDeathArtistTags = new HashSet<>();

    protected DeathArtistTag() {
    }

    public DeathArtistTag(String tagName) {
        this.tagName = tagName;
    }

    public void addDeathArtistDeathArtistTag(DeathArtistDeathArtistTag deathArtistDeathArtistTag) {
        deathArtistDeathArtistTags.add(deathArtistDeathArtistTag);
        deathArtistDeathArtistTag.setDeathArtistTag(this);
    }

}
