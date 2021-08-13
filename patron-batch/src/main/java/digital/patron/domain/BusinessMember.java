package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
public class BusinessMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String status;

    private LocalDate createTime;

    private LocalDate birth;

    private String nationality;

    private String gender;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountInfo accountInfo;

    @OneToMany(mappedBy = "businessMember")
    private List<Artwork> artworks = new ArrayList<>();

    protected BusinessMember(){}

    public BusinessMember(String email, String password, String name, String status, LocalDate createTime, LocalDate birth, String nationality, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.createTime = createTime;
        this.birth = birth;
        this.nationality = nationality;
        this.gender = gender;
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        artwork.setBusinessMember(this);
    }
}
