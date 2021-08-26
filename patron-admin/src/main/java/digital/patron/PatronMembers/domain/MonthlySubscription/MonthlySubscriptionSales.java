package digital.patron.PatronMembers.domain.MonthlySubscription;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class MonthlySubscriptionSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String fee;
    @Column(length = 20)
    private String profit;

    protected MonthlySubscriptionSales() {
    }

    public MonthlySubscriptionSales(String fee, String profit) {
        this.fee = fee;
        this.profit = profit;
    }
}
