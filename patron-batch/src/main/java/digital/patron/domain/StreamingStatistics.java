package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StreamingStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime aggregationTime;

    private String ownerName;

    private int freeNumberOfViews;

    private int paidNumberOfViews;

    private BigDecimal settlementAmount;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;

    private LocalDateTime settlementTIme;

    @OneToOne(fetch = FetchType.LAZY)
    private Tax tax;

    @OneToMany(mappedBy = "streamingStatistics")
    private List<StreamingStatisticsDetail> streamingStatisticsDetailList = new ArrayList<>();

    public enum SettlementStatus {
        WAITING, COMPLETE
    }

}
