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
public class SurviveArtistTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000, name = "name")
    private String tagName;

    @OneToMany(mappedBy = "surviveArtistTag")
    private Set<SurviveArtistSurviveArtistTag> surviveArtistSurviveArtistTags = new HashSet<>();

    protected SurviveArtistTag() {
    }

    public SurviveArtistTag(String tagName) {
        this.tagName = tagName;
    }

    public void addSurviveArtistSurviveArtistTag(SurviveArtistSurviveArtistTag surviveArtistSurviveArtistTag) {
        surviveArtistSurviveArtistTags.add(surviveArtistSurviveArtistTag);
        surviveArtistSurviveArtistTag.setSurviveArtistTag(this);
    }

}
