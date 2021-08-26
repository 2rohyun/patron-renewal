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
public class ArtistGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "name")
    private String artistGroupName;

    @OneToMany(mappedBy = "artistGroup")
    private Set<DeathArtistArtistGroup> deathArtistArtistGroups = new HashSet<>();

    @OneToMany(mappedBy = "artistGroup")
    private Set<SurviveArtistArtistGroup> surviveArtistArtistGroups = new HashSet<>();

    protected ArtistGroup() {
    }

    public ArtistGroup(String artistGroupName) {
        this.artistGroupName = artistGroupName;
    }

    // 연관 관계 편의 메소드 //
    public void addDeathArtistArtistGroup(DeathArtistArtistGroup deathArtistArtistGroup) {
        deathArtistArtistGroups.add(deathArtistArtistGroup);
        deathArtistArtistGroup.setArtistGroup(this);
    }

    public void addSurviveArtistArtistGroup(SurviveArtistArtistGroup surviveArtistArtistGroup) {
        surviveArtistArtistGroups.add(surviveArtistArtistGroup);
        surviveArtistArtistGroup.setArtistGroup(this);
    }
}
