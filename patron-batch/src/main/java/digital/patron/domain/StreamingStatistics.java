package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String ownerName;

    private String ownerEmail;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private int freeNumberOfViews;

    private int paidNumberOfViews;

    private BigDecimal settlementAmount;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;

    private LocalDateTime settlementTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Tax tax;

    @ManyToOne(fetch = FetchType.LAZY)
    private StreamingTotal streamingTotal;

    @OneToMany(mappedBy = "streamingStatistics",cascade = CascadeType.ALL)
    private List<StreamingStatisticsDetail> streamingStatisticsDetailList = new ArrayList<>();

    public enum SettlementStatus {
        WAITING, COMPLETE
    }

    public enum MemberType {
        SALE, BUSINESS
    }

}
