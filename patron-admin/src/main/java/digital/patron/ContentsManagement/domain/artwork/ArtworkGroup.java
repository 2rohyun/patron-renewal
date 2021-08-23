package digital.patron.ContentsManagement.domain.artwork;

import digital.patron.ContentsManagement.utils.BooleanToYNConverter;
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
public class ArtworkGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "name")
    private String groupName;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean showing;

    @OneToMany(mappedBy = "artworkGroup")
    private Set<ArtworkArtworkGroup> artworkArtworkGroups = new HashSet<>();

    protected ArtworkGroup() {
    }

    public ArtworkGroup(String groupName, boolean showing) {
        this.groupName = groupName;
        this.showing = showing;
    }

    // 연관 관계 편의 메소드 //
    public void addArtworkArtworkGroup(ArtworkArtworkGroup artworkArtworkGroup) {
        artworkArtworkGroups.add(artworkArtworkGroup);
        artworkArtworkGroup.setArtworkGroup(this);
    }
}
