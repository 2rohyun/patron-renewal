package digital.patron.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class Manage {
    public enum ManagePermission { NORMAL, SUPER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private ManagePermission permission;

    @Column(nullable = false)
    private LocalDate createTime;

    @Column(nullable = false, length = 300)
    private String password;

    //TODO ( 기업회원과 ManyToOne 단방향 매핑 )

}
