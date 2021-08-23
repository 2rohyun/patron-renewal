package digital.patron.ContentsManagement.domain.exhibition;

import digital.patron.ContentsManagement.utils.BaseTimeEntity;
import digital.patron.ContentsManagement.utils.BooleanToYNConverter;
import digital.patron.ContentsManagement.domain.integrate.ArtworkExhibition;
import digital.patron.ContentsManagement.domain.integrate.DeathArtistExhibition;
import digital.patron.ContentsManagement.domain.integrate.SurviveArtistExhibition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class Exhibition extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(nullable = false, length = 100, name = "name")
    private String exhibitionName;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(nullable = false, length = 100)
    private String organizer;

    @Column(nullable = false, length = 500)
    private String docent;

    @Column(nullable = false, length = 500)
    private String intro;

    private LocalDate startDate;

    private LocalDate endDate;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean showing;

    private int numberOfLikes;

    private int numberOfViews;

    // N : M //
    @OneToMany(mappedBy = "exhibition")
    @OrderBy("exhibitionTag.id asc")
    private Set<ExhibitionExhibitionTag> exhibitionExhibitionTags = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "exhibition")
    private Set<ExhibitionExhibitionGroup> exhibitionExhibitionGroups = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "exhibition")
    @OrderBy("id asc")
    private Set<DeathArtistExhibition> deathArtistExhibitions = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "exhibition")
    @OrderBy("id asc")
    private Set<SurviveArtistExhibition> surviveArtistExhibitions = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "exhibition")
    @OrderBy("id asc")
    private Set<ArtworkExhibition> artworkExhibitions = new HashSet<>();


    // 연관관계 편의 메소드 //

    // 기본 생성자 //
    protected Exhibition() {
    }

    // 실제로 Exhibition 객체를 생성할 때 사용하는 생성자 //
    public Exhibition(String code, String exhibitionName, String type, String organizer, String docent, String intro, LocalDate startDate, LocalDate endDate, boolean showing, int numberOfLikes, int numberOfViews) {
        this.code = code;
        this.exhibitionName = exhibitionName;
        this.type = type;
        this.organizer = organizer;
        this.docent = docent;
        this.intro = intro;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showing = showing;
        this.numberOfLikes = numberOfLikes;
        this.numberOfViews = numberOfViews;
    }

    public void addExhibitionExhibitionTag(ExhibitionExhibitionTag exhibitionExhibitionTag) {
        exhibitionExhibitionTags.add(exhibitionExhibitionTag);
        exhibitionExhibitionTag.setExhibition(this);
    }

    public void addExhibitionExhibitionGroup(ExhibitionExhibitionGroup exhibitionExhibitionGroup) {
        exhibitionExhibitionGroups.add(exhibitionExhibitionGroup);
        exhibitionExhibitionGroup.setExhibition(this);
    }

    public void addDeathArtistExhibition(DeathArtistExhibition deathArtistExhibition) {
        deathArtistExhibitions.add(deathArtistExhibition);
        deathArtistExhibition.setExhibition(this);
    }

    public void addSurviveArtistExhibition(SurviveArtistExhibition surviveArtistExhibition) {
        surviveArtistExhibitions.add(surviveArtistExhibition);
        surviveArtistExhibition.setExhibition(this);
    }

    public void addArtworkExhibition(ArtworkExhibition artworkExhibition) {
        artworkExhibitions.add(artworkExhibition);
        artworkExhibition.setExhibition(this);
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", exhibitionName='" + exhibitionName + '\'' +
                ", type='" + type + '\'' +
                ", organizer='" + organizer + '\'' +
                ", docent='" + docent + '\'' +
                ", intro='" + intro + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", showing=" + showing +
                ", numberOfLikes=" + numberOfLikes +
                ", numberOfViews=" + numberOfViews +
                '}';
    }
}
