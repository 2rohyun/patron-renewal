package digital.patron.PatronMembers.domain;

import digital.patron.PatronMembers.domain.AccountInformation.AccountInfo;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class SaleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100)
    private String name;
    @Column(length = 10)
    private String status;

    private LocalDateTime create_time;

    private LocalDateTime birth;

    @Column(length = 20)
    private String nationality;

    @Column(length = 10)
    private String gender;
    @Column(length = 300)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountInfo accountInfo;
    @OneToMany(mappedBy = "saleMember")
    private Set<MonthlySubscription> monthlySubscriptions = new HashSet<>();

    protected SaleMember() {
    }

    public SaleMember(String email, String name,
                      String status, LocalDateTime create_time,
                      LocalDateTime birth, String nationality,
                      String gender, String password) {
        this.email = email;
        this.name = name;
        this.status = status;
        this.create_time = create_time;
        this.birth = birth;
        this.nationality = nationality;
        this.gender = gender;
        this.password = password;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public void addMonthlySubscription(MonthlySubscription monthlySubscription) {
        monthlySubscriptions.add(monthlySubscription);
        monthlySubscription.setSaleMember(this);
    }

}
