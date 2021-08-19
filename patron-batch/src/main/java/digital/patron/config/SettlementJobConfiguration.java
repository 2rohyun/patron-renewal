package digital.patron.config;

import digital.patron.domain.*;
import digital.patron.dto.ArtworkDto;
import digital.patron.repository.*;
import digital.patron.tasklet.SaveMemberTasklet;
import digital.patron.utils.SettlementJobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final GeneralMemberRepository generalMemberRepository;
    private final SaleMemberRepository saleMemberRepository;
    private final BusinessMemberRepository businessMemberRepository;
    private final StreamingTotalRepository streamingTotalRepository;
    private final StreamingStatisticsRepository streamingStatisticsRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final int CHUNK = 300;

    @Bean
    public Job settlementJob() throws Exception {
        return this.jobBuilderFactory.get("settlementJob")
                .incrementer(new RunIdIncrementer())
                .start(this.saveMemberStep())
                .next(this.totalAmountOfMonthSubscriptionStep(null))
                .next(this.totalNumberOfViewsOfArtworkStep(null))
                // todo ( 권리자별 실 정산 금액 )
                .next(this.actualSettlementAmountBySaleMemberStep(null))
                // todo ( 권리자 작품 별 실 정산 금액 )
//                .next(this.actualSettlementAmountByOwnerArtworksStep())
                // todo ( 집계 기간 총 순익 )
//                .next(this.grossProfitStep())
                .listener(new SettlementJobListener())
                .build();
    }

    @Bean
    public Step saveMemberStep() {
        return this.stepBuilderFactory.get("saveMemberStep")
                .tasklet(new SaveMemberTasklet(generalMemberRepository,saleMemberRepository,businessMemberRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step totalAmountOfMonthSubscriptionStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("totalAmountOfMonthSubscriptionStep")
                .<BigDecimal, StreamingTotal>chunk(CHUNK)
                .reader(totalAmountOfMonthSubscriptionItemReader(date))
                .processor(totalAmountOfMonthSubscriptionItemProcessor(date))
                .writer(totalAmountOfMonthSubscriptionItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step totalNumberOfViewsOfArtworkStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("totalNumberOfViewsOfArtworkStep")
                .<Artwork, StreamingTotal>chunk(CHUNK)
                .reader(totalNumberOfViewsOfArtworkItemReader())
                .processor(totalNumberOfViewsOfArtworkItemProcessor(date))
                .writer(totalNumberOfViewsOfArtworkItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step actualSettlementAmountBySaleMemberStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberStep")
                .<SaleMember, StreamingStatistics>chunk(CHUNK)
                .reader(actualSettlementAmountBySaleMemberItemReader())
                .processor(actualSettlementAmountBySaleMemberItemProcessor(date))
                .writer(actualSettlementAmountBySaleMemberItemWriter())
                .build();

    }

    private ItemReader<? extends BigDecimal> totalAmountOfMonthSubscriptionItemReader(String date) throws Exception {
        Map<String, Object> parameters = getStartDateTimeAndEndDateTimeOfYearMonth(date);

        JpaPagingItemReader<BigDecimal> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select sum(m.amount)" +
                " from MonthSubscription m" +
                " where m.membershipStartTime >= :startDateTime" +
                " and m.membershipStartTime <= :endDateTime");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK);

        reader.afterPropertiesSet();

        return reader;
    }

    private ItemProcessor<? super BigDecimal,? extends StreamingTotal> totalAmountOfMonthSubscriptionItemProcessor(String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        LocalDateTime aggregationEnd = (LocalDateTime) startEndMap.get("endDateTime");

        return sum -> new StreamingTotal(null, aggregationStart, aggregationEnd, sum, 0, 0, null,LocalDateTime.now(), LocalDateTime.now(),null);
    }

    private ItemWriter<? super StreamingTotal> totalAmountOfMonthSubscriptionItemWriter() {
        return streamingTotals -> streamingTotals.forEach(streamingTotalRepository::save);
    }

    private ItemReader<? extends Artwork> totalNumberOfViewsOfArtworkItemReader() throws Exception {
        JpaPagingItemReader<Artwork> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from Artwork a");
        reader.setPageSize(CHUNK);

        reader.afterPropertiesSet();

        return reader;
    }

    private ItemProcessor<? super Artwork,? extends StreamingTotal> totalNumberOfViewsOfArtworkItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));
        // 유, 무료로 골라서 뺀 다음에 StreamingTotal 객체 변경
        ArtworkDto freeArtworkDto = new ArtworkDto();
        ArtworkDto paidArtworkDto = new ArtworkDto();
        return artwork -> {
            if(artwork.isChargeFree()) {
                freeArtworkDto.setTotalNumberOfViews(
                        freeArtworkDto.getTotalNumberOfViews() + (artwork.getNumberOfViews() - artwork.getViewsExcludingThisMonth())
                );
            } else {
                paidArtworkDto.setTotalNumberOfViews(
                        paidArtworkDto.getTotalNumberOfViews() + (artwork.getNumberOfViews() - artwork.getViewsExcludingThisMonth())
                );
            }
            streamingTotal.initTotalFreeNumberOfViews(freeArtworkDto.getTotalNumberOfViews());
            streamingTotal.initTotalPaidNumberOfViews(paidArtworkDto.getTotalNumberOfViews());
            streamingTotal.changeUpdateTime(LocalDateTime.now());

            return streamingTotal;
        };
    }

    private ItemWriter<? super StreamingTotal> totalNumberOfViewsOfArtworkItemWriter() {
        // StreamingTotal 저장
        return streamingTotal -> streamingTotal.forEach(streamingTotalRepository::save);
    }

    private ItemReader<? extends SaleMember> actualSettlementAmountBySaleMemberItemReader() throws Exception {
        JpaPagingItemReader<SaleMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        // todo (warn 해결)
        reader.setQueryString("select s from SaleMember s join fetch s.artworks");

        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super SaleMember,? extends StreamingStatistics> actualSettlementAmountBySaleMemberItemProcessor(String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));

        return saleMember -> {
            int chargeFreeSum = saleMember.getArtworks().stream()
                    .filter(Artwork::isChargeFree)
                    .mapToInt(a -> a.getNumberOfViews() - a.getViewsExcludingThisMonth())
                    .sum();
            int chargePaidSum = saleMember.getArtworks().stream()
                    .filter(a -> !a.isChargeFree())
                    .mapToInt(a2 -> a2.getNumberOfViews() - a2.getViewsExcludingThisMonth())
                    .sum();
            if (streamingTotal.getTotalPaidNumberOfViews() == 0) throw new IllegalArgumentException("Total paid artwork's number of views are 0");

            return new StreamingStatistics(
                    null,
                    saleMember.getName(),
                    saleMember.getEmail(),
                    StreamingStatistics.MemberType.SALE,
                    chargeFreeSum,
                    chargePaidSum,
                    streamingTotal.getTotalSubscriptionAmount()
                                    .multiply(saleMember.getDistributionRatio()
                                    .multiply(BigDecimal.valueOf((double) chargePaidSum / (double) streamingTotal.getTotalPaidNumberOfViews()))),
                    StreamingStatistics.SettlementStatus.WAITING,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    new Tax(null,null,null,null),
                    streamingTotal,
                    null
            );
        };
    }

    private ItemWriter<? super StreamingStatistics> actualSettlementAmountBySaleMemberItemWriter() {
        // todo
        return streamingStatisticsList -> streamingStatisticsList.forEach(streamingStatisticsRepository::save);
    }

    private Map<String, Object> getStartDateTimeAndEndDateTimeOfYearMonth(String date) {
        YearMonth yearMonth = YearMonth.parse(date);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDateTime",yearMonth.atDay(1).atTime(0,0,0));
        parameters.put("endDateTime",yearMonth.atEndOfMonth().atTime(23,59,59));
        return parameters;
    }
}
