package digital.patron.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class SaleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 10)
    private GeneralMember.MemberStatus status;

    @Column(nullable = false)
    private LocalDate createTime;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 20)
    private String nationality;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false, length = 300)
    private String password;

    //TODO AccountInfo OneToOne 단방향 설정
}
