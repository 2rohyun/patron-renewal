package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MonthSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String membership;

    private String membershipPeriod;

    private String amount;

    private String pgAgency;

    private String pgMethod;

    private int approveNumber;

    private LocalDateTime transactionTime;

    private LocalDateTime cancelTime;

    @OneToOne(fetch = FetchType.LAZY)
    private MonthSubscriptionSales monthSubscriptionSales;
}
