package digital.patron.ContentsManagement.domain.artwork;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class ContentsHd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(length = 500)
    private String video;

    @Column(length = 500)
    private String defaultImg;

    @Column(length = 500)
    private String originalImg;

    protected ContentsHd() {
    }

    public ContentsHd(String code, String video, String defaultImg, String originalImg) {
        this.code = code;
        this.video = video;
        this.defaultImg = defaultImg;
        this.originalImg = originalImg;
    }
}
