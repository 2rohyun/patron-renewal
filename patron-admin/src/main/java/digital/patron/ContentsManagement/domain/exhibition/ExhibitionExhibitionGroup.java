package digital.patron.ContentsManagement.domain.exhibition;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "exhibition_exhibition_group_relation")
public class ExhibitionExhibitionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExhibitionGroup exhibitionGroup;

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public void setExhibitionGroup(ExhibitionGroup exhibitionGroup) {
        this.exhibitionGroup = exhibitionGroup;
    }
}
