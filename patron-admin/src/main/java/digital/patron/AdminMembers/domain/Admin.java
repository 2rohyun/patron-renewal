package digital.patron.AdminMembers.domain;

import digital.patron.common.domain.Role;
import digital.patron.common.domain.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 20)
    private String tel;

    private LocalDateTime create_time;
    @Column(length = 300)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public Admin(String email, String tel, LocalDateTime create_time, String password, Status status, Role role) {
        this.email = email;
        this.tel = tel;
        this.create_time = create_time;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    protected Admin() {
    }


}
