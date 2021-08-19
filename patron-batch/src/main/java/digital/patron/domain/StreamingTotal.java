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
public class StreamingTotal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime aggregationStartTime;

    private LocalDateTime aggregationEndTime;

    private BigDecimal totalSubscriptionAmount;

    private int totalFreeNumberOfViews;

    private int totalPaidNumberOfViews;

    private BigDecimal grossProfit;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "streamingTotal")
    private List<StreamingStatistics> streamingStatisticsList = new ArrayList<>();

    public void initTotalFreeNumberOfViews(int totalFreeNumberOfViews){
        this.totalFreeNumberOfViews = totalFreeNumberOfViews;
    }

    public void initTotalPaidNumberOfViews(int totalPaidNumberOfViews){
        this.totalPaidNumberOfViews = totalPaidNumberOfViews;
    }

    public void changeUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
