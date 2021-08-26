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
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "name")
    private String soundName;

    @Column(nullable = false, length = 100, name = "creator")
    private String soundCreator;

    @Column(nullable = false, length = 300, name = "source")
    private String soundSource;

    @Column(nullable = false, length = 100, name = "license")
    private String soundLicense;

    @Column(nullable = false, length = 300, name = "url")
    private String soundUrl;

    protected Sound() {
    }

    public Sound(String soundName, String soundCreator, String soundSource, String soundLicense, String soundUrl) {
        this.soundName = soundName;
        this.soundCreator = soundCreator;
        this.soundSource = soundSource;
        this.soundLicense = soundLicense;
        this.soundUrl = soundUrl;
    }
}
