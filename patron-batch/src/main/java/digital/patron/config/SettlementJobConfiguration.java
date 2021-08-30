package digital.patron.config;

import digital.patron.domain.*;
import digital.patron.dto.ArtworkDto;
import digital.patron.dto.MonthSubscriptionDto;
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
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
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
    private final StreamingStatisticsDetailRepository streamingStatisticsDetailRepository;
    private final ArtworkRepository artworkRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final int CHUNK_SIZE = 100;

    private final TaskExecutor taskExecutor;

    // 집계 및 정산 Job
    @Bean
    public Job settlementJob() throws Exception {
        return this.jobBuilderFactory.get("settlementJob")
                .incrementer(new RunIdIncrementer())
                .start(this.saveMemberStep()) // 샘플 데이터 저장 Step
                .next(this.totalAmountOfMonthSubscriptionStep(null)) // 월 구독 전체 매출액 Step
                .next(this.totalNumberOfViewsOfArtworkStep(null)) // 집계 기간 유, 무료 작품 총 플레이 횟수 Step
                .next(this.actualSettlementAmountBySaleMemberStep(null)) // 권리자(판매 회원)별 실 정산 금액 Step
                .next(this.actualSettlementAMountByBusinessMemberStep(null)) // 권리자(기업 회원)별 실 정산 금액 Step
                .next(this.actualSettlementAmountBySaleMemberArtworksStep(null)) // 권리자(판매 회원) 작품 별 실 정산 금액 Step
                .next(this.actualSettlementAmountByBusinessMemberArtworksStep(null)) // 권리자(기업 회원) 작품 별 실 정산 금액 Step
                .next(this.grossProfitStep(null)) //집계 기간 총 순익 Step
                .next(this.updateViewsExcludingThisMonthOfArtworkStep())// 작품 이번 달 제외 조회 수 업데이트 Step
                .listener(new SettlementJobListener(streamingStatisticsRepository)) // 총 배치 소요 시간 및 업데이트된 데이터의 건수 listener
                .build();
    }

    /**
     * 샘플 데이터 저장 Step
     * 배치가 제대로 돌아가는지 확인하기 위한 샘플 데이터 저장 Step 으로, tasklet 기반
     * GeneralMember, SaleMember, BusinessMember 각 3000 건씩 저장
     */
    @Bean
    public Step saveMemberStep() {
        return this.stepBuilderFactory.get("saveMemberStep")
                .tasklet(new SaveMemberTasklet(generalMemberRepository,saleMemberRepository,businessMemberRepository))
                .build();
    }

    /**
     * 월 구독 전체 매출액 Step
     * 한 달간 모든 사용자의 월 구독 전체 매출을 구하는 Step 으로, chunk 기반
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step totalAmountOfMonthSubscriptionStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("totalAmountOfMonthSubscriptionStep")
                .<MonthSubscriptionDto, StreamingTotal>chunk(CHUNK_SIZE)
                .reader(totalAmountOfMonthSubscriptionItemReader(date))
                .processor(totalAmountOfMonthSubscriptionItemProcessor(date))
                .writer(totalAmountOfMonthSubscriptionItemWriter())
                .build();
    }

    /**
     * 집계 기간 유, 무료 작품 총 플레이 횟수 Step
     * 집계 기간 ( 현재 구현 기준 한 달 ) 내의 유, 무료 작품 총 플레이 횟수를 구하는 Step 으로, chunk 기반
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step totalNumberOfViewsOfArtworkStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("totalNumberOfViewsOfArtworkStep")
                .<Artwork, StreamingTotal>chunk(CHUNK_SIZE)
                .reader(totalNumberOfViewsOfArtworkItemReader())
                .processor(totalNumberOfViewsOfArtworkItemProcessor(date))
                .writer(totalNumberOfViewsOfArtworkItemWriter())
                .build();
    }

    /**
     * 권리자(판매 회원)별 실 정산 금액 Step
     * 판매 회원이 가지고 있는 작품들에 따라 실 정산 금액을 구하는 Step 으로, chunk 기반
     * chunk size ( 현재 100 ) 별로 10개의 쓰레드가 동시에 task 를 수행한다.
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step actualSettlementAmountBySaleMemberStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberStep")
                .<SaleMember, StreamingStatistics>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountBySaleMemberItemReader())
                .processor(actualSettlementAmountBySaleMemberItemProcessor(date))
                .writer(actualSettlementAmountBySaleMemberItemWriter())
                .taskExecutor(this.taskExecutor)
                .throttleLimit(10)
                .build();

    }

    /**
     * 권리자(기업 회원)별 실 정산 금액 Step
     * 기업 회원이 가지고 있는 작품들에 따라 실 정산 금액을 구하는 Step 으로, chunk 기반
     * chunk size ( 현재 100 ) 별로 10개의 쓰레드가 동시에 task 를 수행한다.
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step actualSettlementAMountByBusinessMemberStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAMountByBusinessMemberStep")
                .<BusinessMember, StreamingStatistics>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountByBusinessMemberItemReader())
                .processor(actualSettlementAmountByBusinessMemberItemProcessor(date))
                .writer(actualSettlementAmountByBusinessMemberItemWriter())
                .taskExecutor(this.taskExecutor)
                .throttleLimit(10)
                .build();
    }

    /**
     * 권리자(판매 회원) 작품 별 실 정산 금액 Step
     * 판매 회원이 가지고 있는 각 작품 별로 실 정산 금액을 구하는 Step 으로, chunk 기반
     * chunk size ( 현재 100 ) 별로 10개의 쓰레드가 동시에 task 를 수행한다.
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step actualSettlementAmountBySaleMemberArtworksStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberArtworksStep")
                .<SaleMember, List<StreamingStatisticsDetail>>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountBySaleMemberArtworkItemReader())
                .processor(actualSettlementAmountBySaleMemberArtworkItemProcessor(date))
                .writer(actualSettlementAmountBySaleMemberArtworkItemWriter())
                .taskExecutor(this.taskExecutor)
                .throttleLimit(10)
                .build();
    }

    /**
     * 권리자(기업 회원) 작품 별 실 정산 금액 Step
     * 기업 회원이 가지고 있는 각 작품 별로 실 정산 금액을 구하는 Step 으로, chunk 기반
     * chunk size ( 현재 100 ) 별로 10개의 쓰레드가 동시에 task 를 수행한다.
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step actualSettlementAmountByBusinessMemberArtworksStep(@Value("#{jobParameters[date]}") String date) throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountByBusinessMemberArtworksStep")
                .<BusinessMember, List<StreamingStatisticsDetail>>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountByBusinessMemberArtworkItemReader())
                .processor(actualSettlementAmountByBusinessMemberArtworkItemProcessor(date))
                .writer(actualSettlementAmountByBusinessMemberArtworkItemWriter())
                .taskExecutor(this.taskExecutor)
                .throttleLimit(10)
                .build();
    }

    /**
     * 집계 기간 총 순익 Step
     * 집계 기간동안 작품 권리자들에게 정산을 해주고, 결제 수수료까지 제외한 총 순 이익을 구하는 Step 으로, chunk 기반
     * @param date : job parameter 로 초기 배치 실행 시 program argument 에 넣어준다. ( ex. -date=2021.08 )
     */
    @Bean
    @JobScope
    public Step grossProfitStep(@Value("#{jobParameters[date]}") String date) throws Exception{
        return this.stepBuilderFactory.get("grossProfitStep")
                .<BigDecimal,StreamingTotal>chunk(CHUNK_SIZE)
                .reader(grossProfitItemReader())
                .processor(grossProfitItemProcessor(date))
                .writer(grossProfitItemWriter())
                .build();
    }

    /**
     * 작품 이번 달 제외 조회 수 업데이트 Step
     * 모든 집계 및 정산 Step 이 끝난 뒤, 다음 달의 조회 수를 산정하기 위해,
     * Artwork 의 viewsExcludingThisMonth 컬럼을 이번 달 조회 수로 업데이트하는 Step 으로, chunk 기반
     */
    @Bean
    public Step updateViewsExcludingThisMonthOfArtworkStep() throws Exception {
        return this.stepBuilderFactory.get("updateViewsExcludingThisMonthOfArtworkStep")
                .<Artwork, Artwork>chunk(CHUNK_SIZE)
                .reader(updateViewsExcludingThisMonthOfArtworkItemReader())
                .writer(updateViewsExcludingThisMonthOfArtworkItemWriter())
                .taskExecutor(this.taskExecutor)
                .throttleLimit(10)
                .build();
    }

    private ItemReader<? extends MonthSubscriptionDto> totalAmountOfMonthSubscriptionItemReader(String date) throws Exception {
        Map<String, Object> parameters = getStartDateTimeAndEndDateTimeOfYearMonth(date);

        JpaPagingItemReader<MonthSubscriptionDto> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "select new digital.patron.dto.MonthSubscriptionDto(sum(m.amount), sum(s.profit))" +
                " from MonthSubscription m join m.monthSubscriptionSales s" +
                " where m.membershipStartTime >= :startDateTime" +
                " and m.membershipStartTime <= :endDateTime");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);
        reader.afterPropertiesSet();

        return reader;
    }

    private ItemProcessor<? super MonthSubscriptionDto,? extends StreamingTotal> totalAmountOfMonthSubscriptionItemProcessor(String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        LocalDateTime aggregationEnd = (LocalDateTime) startEndMap.get("endDateTime");

        return monthSubscriptionDto -> new StreamingTotal(
                    null,
                    aggregationStart,
                    aggregationEnd,
                    monthSubscriptionDto.getTotalAmount(),
                    monthSubscriptionDto.getTotalAmountExceptFee(),
                    0,
                    0,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null
        );

    }

    private ItemWriter<? super StreamingTotal> totalAmountOfMonthSubscriptionItemWriter() {
        return streamingTotals -> streamingTotals.forEach(streamingTotalRepository::save);
    }

    private ItemReader<? extends Artwork> totalNumberOfViewsOfArtworkItemReader() throws Exception {
        JpaPagingItemReader<Artwork> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from Artwork a");
        reader.setPageSize(CHUNK_SIZE);
        reader.afterPropertiesSet();

        return reader;
    }

    private ItemProcessor<? super Artwork,? extends StreamingTotal> totalNumberOfViewsOfArtworkItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));

        // 작품에서 유, 무료로 골라서 조회 수를 각각 구한 뒤, StreamingTotal 객체에 추가
        ArtworkDto freeArtworkDto = new ArtworkDto(0,0);
        ArtworkDto paidArtworkDto = new ArtworkDto(0,0);
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
        reader.setQueryString("select s from SaleMember s");
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super SaleMember,? extends StreamingStatistics> actualSettlementAmountBySaleMemberItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));

        // saleMember 가 소유한 작품들의 유, 무료 작품 별 총 조회 수를 계산하여 StreamingStatistics 객체 생성
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
        return streamingStatisticsRepository::saveAll;
    }

    private ItemReader<? extends BusinessMember> actualSettlementAmountByBusinessMemberItemReader() throws Exception {
        JpaPagingItemReader<BusinessMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select b from BusinessMember b");
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super BusinessMember,? extends StreamingStatistics> actualSettlementAmountByBusinessMemberItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));

        // businessMember 가 소유한 작품들의 유, 무료 작품 별 총 조회 수, 정산 금액을 계산하여 StreamingStatistics 객체 생성
        return businessMember -> {
            int chargeFreeSum = businessMember.getArtworks().stream()
                    .filter(Artwork::isChargeFree)
                    .mapToInt(a -> a.getNumberOfViews() - a.getViewsExcludingThisMonth())
                    .sum();
            int chargePaidSum = businessMember.getArtworks().stream()
                    .filter(a -> !a.isChargeFree())
                    .mapToInt(a2 -> a2.getNumberOfViews() - a2.getViewsExcludingThisMonth())
                    .sum();
            if (streamingTotal.getTotalPaidNumberOfViews() == 0) throw new IllegalArgumentException("Total paid artwork's number of views are 0");

            return new StreamingStatistics(
                    null,
                    businessMember.getName(),
                    businessMember.getEmail(),
                    StreamingStatistics.MemberType.BUSINESS,
                    chargeFreeSum,
                    chargePaidSum,
                    streamingTotal.getTotalSubscriptionAmount()
                                    .multiply(businessMember.getDistributionRatio()
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

    private ItemWriter<? super StreamingStatistics> actualSettlementAmountByBusinessMemberItemWriter() {
        return streamingStatisticsList -> streamingStatisticsList.forEach(streamingStatisticsRepository::save);
    }

    private ItemReader<? extends SaleMember> actualSettlementAmountBySaleMemberArtworkItemReader() throws Exception {
        JpaPagingItemReader<SaleMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select s from SaleMember s");
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super SaleMember,? extends List<StreamingStatisticsDetail>> actualSettlementAmountBySaleMemberArtworkItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date!"));

        // saleMember 가 소유한 각 유료 작품 당 조회 수, 정산 금액 계산 후 StreamingStatisticsDetail 객체 생성
        return saleMember -> {
            StreamingStatistics streamingStatistics = streamingStatisticsRepository.findByOwnerEmail(saleMember.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("No streaming statistics found for owner's email!"));

            return saleMember.getArtworks().stream()
                    .filter(a -> !a.isChargeFree())
                    .map(p -> new StreamingStatisticsDetail(
                            null,
                            p.getName(),
                            p.getNumberOfViews() - p.getViewsExcludingThisMonth(),
                            streamingTotal.getTotalSubscriptionAmount()
                                    .multiply(saleMember.getDistributionRatio())
                                    .multiply(BigDecimal.valueOf((double) (p.getNumberOfViews() - p.getViewsExcludingThisMonth()) / (double) streamingTotal.getTotalPaidNumberOfViews())),
                            streamingStatistics
                    ))
                    .collect(Collectors.toList());
        };
    }

    private ItemWriter<? super List<StreamingStatisticsDetail>> actualSettlementAmountBySaleMemberArtworkItemWriter() {
        return streamingStatisticsDetails -> streamingStatisticsDetails.forEach(streamingStatisticsDetailRepository::saveAll);
    }

    private ItemReader<? extends BusinessMember> actualSettlementAmountByBusinessMemberArtworkItemReader() throws Exception{
        JpaPagingItemReader<BusinessMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select b from BusinessMember b");
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super BusinessMember,? extends List<StreamingStatisticsDetail>> actualSettlementAmountByBusinessMemberArtworkItemProcessor(String date) {
        // StreamingTotal 찾아오기
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date!"));

        // businessMember 가 소유한 각 유료 작품 당 조회 수, 정산 금액 계산 후 StreamingStatisticsDetail 객체 생성
        return businessMember -> {
            StreamingStatistics streamingStatistics = streamingStatisticsRepository.findByOwnerEmail(businessMember.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("No streaming statistics found for owner's email!"));

            return businessMember.getArtworks().stream()
                    .filter(a -> !a.isChargeFree())
                    .map(p -> new StreamingStatisticsDetail(
                            null,
                            p.getName(),
                            p.getNumberOfViews() - p.getViewsExcludingThisMonth(),
                            streamingTotal.getTotalSubscriptionAmount()
                                    .multiply(businessMember.getDistributionRatio())
                                    .multiply(BigDecimal.valueOf((double) (p.getNumberOfViews() - p.getViewsExcludingThisMonth()) / (double) streamingTotal.getTotalPaidNumberOfViews())),
                            streamingStatistics
                    ))
                    .collect(Collectors.toList());
        };
    }

    private ItemWriter<? super List<StreamingStatisticsDetail>> actualSettlementAmountByBusinessMemberArtworkItemWriter() {
        return streamingStatisticsDetails -> streamingStatisticsDetails.forEach(streamingStatisticsDetailRepository::saveAll);
    }

    private ItemReader<? extends BigDecimal> grossProfitItemReader() throws Exception{
        JpaPagingItemReader<BigDecimal> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select sum(s.settlementAmount) from StreamingStatistics s");
        reader.setPageSize(CHUNK_SIZE);
        reader.afterPropertiesSet();
        return reader;
    }

    private ItemProcessor<? super BigDecimal,? extends StreamingTotal> grossProfitItemProcessor(String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date!"));

        return sum -> {
            streamingTotal.initGrossProfit(streamingTotal.getTotalSubscriptionAmountExceptFee().subtract(sum));
            streamingTotal.changeUpdateTime(LocalDateTime.now());
            return streamingTotal;
        };
    }

    private ItemWriter<? super StreamingTotal> grossProfitItemWriter() {
        return streamingTotals -> streamingTotals.forEach(streamingTotalRepository::save);
    }

    private ItemReader<? extends Artwork> updateViewsExcludingThisMonthOfArtworkItemReader() throws Exception {
        JpaPagingItemReader<Artwork> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from Artwork a");
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();

        return reader;
    }

    private ItemWriter<? super Artwork> updateViewsExcludingThisMonthOfArtworkItemWriter() {
        return artworks -> artworks.forEach(a-> {
            a.changeViewsExcludingThisMonth(a.getNumberOfViews());
            artworkRepository.save(a);
        });
    }

    private Map<String, Object> getStartDateTimeAndEndDateTimeOfYearMonth(String date) {
        YearMonth yearMonth = YearMonth.parse(date);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDateTime",yearMonth.atDay(1).atTime(0,0,0));
        parameters.put("endDateTime",yearMonth.atEndOfMonth().atTime(23,59,59));
        return parameters;
    }
}
