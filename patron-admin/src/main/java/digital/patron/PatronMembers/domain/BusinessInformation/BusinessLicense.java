package digital.patron.PatronMembers.domain.BusinessInformation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class BusinessLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 20)
    private String tel;
    @Column(length = 20)
    private String create_time;

    protected BusinessLicense(){}

    public BusinessLicense(String email, String tel, String create_time) {
        this.email = email;
        this.tel = tel;
        this.create_time = create_time;
    }
}
