package digital.patron.PatronMembers.domain.BusinessInformation;

import digital.patron.PatronMembers.domain.BusinessMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class BusinessManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100)
    private String name;
    @Column(length = 20)
    private String permission;

    @Column(length = 20)
    private String phone_number;

    private LocalDateTime create_time;
    @Column(length = 300)
    private String password;


    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessMember businessMember;

    protected BusinessManager() {

    }

    public BusinessManager(String email, String name, String permission, LocalDateTime create_time, String password, String phone_number) {
        this.email = email;
        this.name = name;
        this.permission = permission;
        this.create_time = create_time;
        this.password = password;
        this.phone_number = phone_number;
    }

    public void setBusinessMember(BusinessMember businessMember) {
        this.businessMember = businessMember;
    }
}
