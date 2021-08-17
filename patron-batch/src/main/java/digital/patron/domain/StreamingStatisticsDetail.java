package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StreamingStatisticsDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artworkName;

    private int numberOfViewsPerArtwork;

    private BigDecimal settlementAmountPerArtwork;

    @ManyToOne(fetch = FetchType.LAZY)
    private StreamingStatistics streamingStatistics;

}
