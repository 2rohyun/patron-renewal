package digital.patron.ContentsManagement.domain.artwork;

import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import digital.patron.ContentsManagement.utils.BaseTimeEntity;
import digital.patron.ContentsManagement.utils.BooleanToYNConverter;
import digital.patron.ContentsManagement.domain.integrate.ArtworkExhibition;
import digital.patron.PatronMembers.domain.BusinessMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class Artwork extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(nullable = false, length = 100, name = "name")
    private String artworkName;

    @Column(nullable = false, length = 500)
    private String intro;

    @Column(nullable = false, length = 100)
    private String size;

    @Column(nullable = false, length = 10)
    private String year;

    @Column(nullable = false, length = 50)
    private String keep;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean approve;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean showing;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean represent;

    private LocalDateTime registeredAt;

    private int numberOfLikes;

    private int numberOfViews;

    // 양방향 매핑, 연관관계의 주인 X, 따라서 외래키를 가지고 있지 않음 //
    // 양방향 매핑인 이유 : 작품의 재료, 태그의 경우 작품 -> 재료, 태그로 데이터를 찾아 들어가는 경우가 많다. 또한,
    //                  작품이 포함된 전시, 작품이 포함된 그룹은 작품 -> 전시/그룹, 전시/그룹 -> 작가 양방향으로 데이터를 찾기 때문에 서로를 알아야 한다, //
    // List 대신 Set 을 사용하는 이유 : https://jojoldu.tistory.com/165
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private Set<ArtworkSource> artworkSources = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "artwork")
    @OrderBy("artworkTag.id asc")
    private Set<ArtworkArtworkTag> artworkArtworkTags = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "artwork")
    private Set<ArtworkExhibition> artworkExhibitions = new HashSet<>();

    // N : M 매핑 //
    @OneToMany(mappedBy = "artwork")
    private Set<ArtworkArtworkGroup> artworkArtworkGroups = new HashSet<>();

    // 단방향 매핑, 연관관계의 주인 O, 따라서 외래키를 가지고 있음 //
    // 단방향 매핑인 이유 : 작품의 사운드/컨텐츠는 작품 -> 사운드/컨텐츠로 데이터를 찾기 때문에 사운드/컨텐츠는 작품의 존재를 알 필요가 없다. //
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Sound sound;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contents4k contents4k;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ContentsHd contentsHd;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ContentsThumbnail contentsThumbnail;


    // 양방향 매핑, 연관관계의 주인 O, 따라서 외래키를 가지고 있음 //
    // 양방향 매핑인 이유 : 작품과 작가의 관계는 작품 -> 작가, 작가 -> 작품 양쪽으로 데이터를 찾는 경우가 많기 때문에 서로의 존재를 알고 있어야 한다. //
    @ManyToOne(fetch = FetchType.LAZY)
    private SurviveArtist surviveArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeathArtist deathArtist;


    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessMember businessMember;

    public void setBusinessMember(BusinessMember businessMember) {
        this.businessMember = businessMember;
    }

    // 기본 생성자 //
    // protected 인 이유 : 실제로 사용하진 않지만, JPA 스펙 상 선언해두어야 함. //
    protected Artwork() {
    }

    // 실질적으로 Artwork 객체를 생성하기 위한 생성자 //
    public Artwork(String code, String artworkName, String intro, String size, String year, String keep, boolean approve, boolean showing, boolean represent, int numberOfLikes, int numberOfViews, LocalDateTime registeredAt) {
        this.code = code;
        this.artworkName = artworkName;
        this.intro = intro;
        this.size = size;
        this.year = year;
        this.keep = keep;
        this.approve = approve;
        this.showing = showing;
        this.represent = represent;
        this.numberOfLikes = numberOfLikes;
        this.numberOfViews = numberOfViews;
        this.registeredAt = registeredAt;
    }

    // 양방향 연관 관계 시, 편하게 양쪽에 값을 세팅하기 위한 편의 메소드 //
    public void addArtworkSource(ArtworkSource artworkSource) {
        artworkSources.add(artworkSource);
        artworkSource.setArtwork(this);
    }

    public void addArtworkArtworkTag(ArtworkArtworkTag artworkArtworkTag) {
        artworkArtworkTags.add(artworkArtworkTag);
        artworkArtworkTag.setArtwork(this);
    }

    public void addArtworkExhibition(ArtworkExhibition artworkExhibition) {
        artworkExhibitions.add(artworkExhibition);
        artworkExhibition.setArtwork(this);
    }

    public void addArtworkArtworkGroup(ArtworkArtworkGroup artworkArtworkGroup) {
        artworkArtworkGroups.add(artworkArtworkGroup);
        artworkArtworkGroup.setArtwork(this);
    }


    // 단방향 연관 관계 시, 전체 setter 를 닫았기 때문에 선택적으로 필요한 setter 만 열어줌. //
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setContents4k(Contents4k contents4k) {
        this.contents4k = contents4k;
    }

    public void setContentsHd(ContentsHd contentsHd) {
        this.contentsHd = contentsHd;
    }

    public void setContentsThumbnail(ContentsThumbnail contentsThumbnail) {
        this.contentsThumbnail = contentsThumbnail;
    }

    // 양방향 연관 관계 시, @OneToMany 인 엔티티에 연관 관계 편의 메소드를 구현하므로 setter 필요. //
    public void setDeathArtist(DeathArtist deathArtist) {
        this.deathArtist = deathArtist;
    }

    public void setSurviveArtist(SurviveArtist surviveArtist) {
        this.surviveArtist = surviveArtist;
    }

    @Override
    public String toString() {
        return "Artwork{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", artworkName='" + artworkName + '\'' +
                ", intro='" + intro + '\'' +
                ", size='" + size + '\'' +
                ", year='" + year + '\'' +
                ", keep='" + keep + '\'' +
                ", approve=" + approve +
                ", showing=" + showing +
                ", registeredAt=" + registeredAt +
                ", numberOfLikes=" + numberOfLikes +
                ", numberOfViews=" + numberOfViews +
                ", surviveArtist=" + surviveArtist +
                ", deathArtist=" + deathArtist +
                '}';
    }

    public void increaseNumberOfLikes() {
        this.numberOfLikes += 1;
    }

    public void increaseNumberOfViews() {
        this.numberOfViews += 1;
    }

    public void decreaseNumberOfLikes() {
        if (this.numberOfLikes > 0) {
            this.numberOfLikes -= 1;
        }
    }
}
