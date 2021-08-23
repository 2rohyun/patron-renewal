package digital.patron.PatronMembers.domain.MonthlySubscription;

import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.SaleMember;
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
public class MonthlySubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String membership;
    @Column(length = 100)
    private String membership_period;
    @Column(length = 10)
    private String amount;
    @Column(length = 100)
    private String pg_agency;
    @Column(length = 20)
    private String pg_method;

    private Integer approve_number;

    private LocalDateTime transaction_time;

    private LocalDateTime cancel_time;

    @ManyToOne(fetch = FetchType.LAZY)
    private GeneralMember generalMember;
    @ManyToOne(fetch = FetchType.LAZY)
    private SaleMember saleMember;
    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessMember businessMember;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MonthlySubscriptionSales monthlySubscriptionSales;


    protected MonthlySubscription() {

    }

    public MonthlySubscription(String membership, String membership_period,
                               String amount, String pg_agency,
                               String pg_method, Integer approve_number,
                               LocalDateTime transaction_time, LocalDateTime cancel_time) {
        this.membership = membership;
        this.membership_period = membership_period;
        this.amount = amount;
        this.pg_agency = pg_agency;
        this.pg_method = pg_method;
        this.approve_number = approve_number;
        this.transaction_time = transaction_time;
        this.cancel_time = cancel_time;
    }

    public void setMonthlySubscriptionSales(MonthlySubscriptionSales monthlySubscriptionSales) {
        this.monthlySubscriptionSales = monthlySubscriptionSales;
    }

    public void setGeneralMember(GeneralMember generalMember) {
        this.generalMember = generalMember;
    }

    public void setSaleMember(SaleMember saleMember) {
        this.saleMember = saleMember;
    }

    public void setBusinessMember(BusinessMember businessMember) {
        this.businessMember = businessMember;
    }
}
