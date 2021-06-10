package digital.patron.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class GeneralMember {
    public enum MemberStatus { NORMAL, INACTIVE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 10)
    private MemberStatus status;

    @Column(nullable = false)
    private LocalDate createTime;

    @Column(nullable = false)
    private LocalDate lastLogin;

    @Column(nullable = false, length = 300)
    private String publicWallet;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 20)
    private String nationality;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false, length = 300)
    private String password;

}
