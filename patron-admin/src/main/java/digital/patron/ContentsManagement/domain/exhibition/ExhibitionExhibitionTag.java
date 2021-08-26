package digital.patron.ContentsManagement.domain.exhibition;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "exihibition_exhibition_tag_relation")
public class ExhibitionExhibitionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExhibitionTag exhibitionTag;

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public void setExhibitionTag(ExhibitionTag exhibitionTag) {
        this.exhibitionTag = exhibitionTag;
    }
}
