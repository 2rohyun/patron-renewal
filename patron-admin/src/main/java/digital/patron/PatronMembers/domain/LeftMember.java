package digital.patron.PatronMembers.domain;

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
public class LeftMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100)
    private String reason;
    @Column(length = 10)
    private String type;
    private LocalDateTime leave_date;
    @Column(length = 300)
    private String password;

    protected LeftMember() {
    }

    public LeftMember(String email, String reason, String type, LocalDateTime leave_date, String password) {
        this.email = email;
        this.reason = reason;
        this.type = type;
        this.leave_date = leave_date;
        this.password = password;
    }
}
