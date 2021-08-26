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
public class ExhibitionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "name")
    private String groupName;

    @OneToMany(mappedBy = "exhibitionGroup")
    private Set<ExhibitionExhibitionGroup> exhibitionExhibitionGroups = new HashSet<>();

    protected ExhibitionGroup() {
    }

    public ExhibitionGroup(String groupName) {
        this.groupName = groupName;
    }

    public void addExhibitionExhibitionGroup(ExhibitionExhibitionGroup exhibitionExhibitionGroup) {
        exhibitionExhibitionGroups.add(exhibitionExhibitionGroup);
        exhibitionExhibitionGroup.setExhibitionGroup(this);
    }

}
