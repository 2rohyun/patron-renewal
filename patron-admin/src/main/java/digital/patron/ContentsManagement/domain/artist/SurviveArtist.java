package digital.patron.ContentsManagement.domain.artist;

import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.utils.BooleanToYNConverter;
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
public class SurviveArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(length = 100, name = "en_name")
    private String engName;

    @Column(length = 100, name = "ko_name")
    private String korName;

    @Column(length = 500)
    private String profileImg;

    @Column(length = 500)
    private String resume;

    @Column(length = 500)
    private String intro;

    private LocalDate birth;

    @Column(length = 100)
    private String nationality;

    @Column(length = 10)
    private String gender;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean showing;

    private int numberOfLikes;

    // 양방향 연관관계 //

    @OneToMany(mappedBy = "surviveArtist")
    private Set<Artwork> artworks = new HashSet<>();

    @OneToMany(mappedBy = "surviveArtist")
    private Set<SurviveArtistExhibition> surviveArtistExhibitions = new HashSet<>();

    @OneToMany(mappedBy = "surviveArtist")
    private Set<SurviveArtistArtistGroup> surviveArtistArtistGroups = new HashSet<>();

    @OneToMany(mappedBy = "surviveArtist")
    @OrderBy("surviveArtistTag.id asc")
    private Set<SurviveArtistSurviveArtistTag> surviveArtistSurviveArtistTags = new HashSet<>();

    // 양방향 연관관계 시, 편하게 양쪽에 값을 세팅하기 위한 편의 메소드 //

    // 기본 생성자 //
    protected SurviveArtist() {
    }

    public SurviveArtist(String code, String engName, String korName, String profileImg, String resume, String intro, LocalDate birth, String nationality, String gender, boolean showing, int numberOfLikes) {
        this.code = code;
        this.engName = engName;
        this.korName = korName;
        this.profileImg = profileImg;
        this.resume = resume;
        this.intro = intro;
        this.birth = birth;
        this.nationality = nationality;
        this.gender = gender;
        this.showing = showing;
        this.numberOfLikes = numberOfLikes;
    }

    public void addSurviveArtistSurviveArtistTag(SurviveArtistSurviveArtistTag SurviveArtistSurviveArtistTag) {
        surviveArtistSurviveArtistTags.add(SurviveArtistSurviveArtistTag);
        SurviveArtistSurviveArtistTag.setSurviveArtist(this);
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        artwork.setSurviveArtist(this);
    }

    public void addSurviveArtistExhibition(SurviveArtistExhibition surviveArtistExhibition) {
        surviveArtistExhibitions.add(surviveArtistExhibition);
        surviveArtistExhibition.setSurviveArtist(this);
    }

    public void addSurviveArtistArtistGroup(SurviveArtistArtistGroup surviveArtistArtistGroup) {
        surviveArtistArtistGroups.add(surviveArtistArtistGroup);
        surviveArtistArtistGroup.setSurviveArtist(this);
    }

    @Override
    public String toString() {
        return "SurviveArtist{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", engName='" + engName + '\'' +
                ", korName='" + korName + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", resume='" + resume + '\'' +
                ", intro='" + intro + '\'' +
                ", birth=" + birth +
                ", nationality='" + nationality + '\'' +
                ", gender='" + gender + '\'' +
                ", showing=" + showing +
                ", numberOfLikes=" + numberOfLikes +
                '}';
    }
}
