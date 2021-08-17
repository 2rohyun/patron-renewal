package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private LocalDateTime membershipStartedAt;

    private LocalDateTime membershipEndedAt;

    private BigDecimal amount;

    private String pgAgency;

    private String pgMethod;

    private int approveNumber;

    private LocalDateTime transactionTime;

    private LocalDateTime cancelTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MonthSubscriptionSales monthSubscriptionSales;

}
