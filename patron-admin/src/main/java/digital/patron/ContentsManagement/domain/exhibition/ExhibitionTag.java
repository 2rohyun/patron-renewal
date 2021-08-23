package digital.patron.ContentsManagement.domain.exhibition;

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
public class ExhibitionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000, name = "name")
    private String tagName;

    @OneToMany(mappedBy = "exhibitionTag")
    private Set<ExhibitionExhibitionTag> exhibitionExhibitionTags = new HashSet<>();

    protected ExhibitionTag() {
    }

    public ExhibitionTag(String tagName) {
        this.tagName = tagName;
    }

    public void addExhibitionExhibitionTag(ExhibitionExhibitionTag exhibitionExhibitionTag) {
        exhibitionExhibitionTags.add(exhibitionExhibitionTag);
        exhibitionExhibitionTag.setExhibitionTag(this);
    }

}
