package digital.patron.config;

import digital.patron.domain.*;
import digital.patron.dto.ArtworkDto;
import digital.patron.dto.MonthSubscriptionDto;
import digital.patron.partitioner.ArtworkPartitioner;
import digital.patron.partitioner.BusinessMemberPartitioner;
import digital.patron.partitioner.SaleMemberPartitioner;
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
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
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


    //todo [성능 최적화 부분]
    // 1.(화) Async, MultiThread, Parallel, Partition 각각 적용하고 무엇이 제일 빠른지 확인 후 적용

    //todo [etc]
    // 2.(수,목,금) 젠킨스 붙히기
    // 3.(월,화) 문서화
    // 4.(남는 시간) 테스트 코드 작성

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
    private final TaskExecutor taskExecutor;
    private final int CHUNK_SIZE = 100;

    @Bean
    public Job settlementJob() throws Exception {
        return this.jobBuilderFactory.get("settlementJob")
                .incrementer(new RunIdIncrementer())
                .start(this.saveMemberStep()) // 샘플 데이터 저장
                .next(this.totalAmountOfMonthSubscriptionStep(null)) // 월 구독 전체 매출액
                .next(this.totalNumberOfViewsOfArtworkStep(null)) // 집계 기간 유, 무료 작품 총 플레이 횟수
                .next(this.actualSettlementAmountBySaleMemberManagerStep()) // 권리자(판매 회원)별 실 정산 금액
                .next(this.actualSettlementAmountByBusinessMemberManagerStep()) // 권리자(기업 회원)별 실 정산 금액
                .next(this.actualSettlementAmountBySaleMemberArtworksManagerStep()) // 권리자(판매 회원) 작품 별 실 정산 금액
                .next(this.actualSettlementAmountByBusinessMemberArtworksManagerStep()) // 권리자(기업 회원) 작품 별 실 정산 금액
                .next(this.grossProfitStep(null)) //집계 기간 총 순익
                .next(this.updateViewsExcludingThisMonthOfArtworkManagerStep())// 작품 이번 달 제외 조회 수 업데이트
                .listener(new SettlementJobListener(streamingStatisticsRepository))
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
                .<MonthSubscriptionDto, StreamingTotal>chunk(CHUNK_SIZE)
                .reader(totalAmountOfMonthSubscriptionItemReader(date))
                .processor(totalAmountOfMonthSubscriptionItemProcessor(date))
                .writer(totalAmountOfMonthSubscriptionItemWriter())
                .build();
    }

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

    @Bean
    public Step actualSettlementAmountBySaleMemberStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberStep")
                .<SaleMember, StreamingStatistics>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountBySaleMemberItemReader(null,null))
                .processor(actualSettlementAmountBySaleMemberItemProcessor(null))
                .writer(actualSettlementAmountBySaleMemberItemWriter())
                .build();

    }

    @Bean
    public Step actualSettlementAmountBySaleMemberManagerStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberManagerStep")
                .partitioner("actualSettlementAmountBySaleMemberStep", new SaleMemberPartitioner(saleMemberRepository))
                .step(actualSettlementAmountBySaleMemberStep())
                .partitionHandler(SaleMemberTaskExecutorPartitionHandler())
                .build();
    }

    @Bean
    public Step actualSettlementAmountByBusinessMemberStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAMountByBusinessMemberStep")
                .<BusinessMember, StreamingStatistics>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountByBusinessMemberItemReader(null,null))
                .processor(actualSettlementAmountByBusinessMemberItemProcessor(null))
                .writer(actualSettlementAmountByBusinessMemberItemWriter())
                .build();
    }

    @Bean
    public Step actualSettlementAmountByBusinessMemberManagerStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountByBusinessMemberManagerStep")
                .partitioner("actualSettlementAMountByBusinessMemberStep", new BusinessMemberPartitioner(businessMemberRepository))
                .step(actualSettlementAmountByBusinessMemberStep())
                .partitionHandler(BusinessMemberTaskExecutorPartitionHandler())
                .build();
    }

    @Bean
    public Step actualSettlementAmountBySaleMemberArtworksStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberArtworksStep")
                .<SaleMember, List<StreamingStatisticsDetail>>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountBySaleMemberArtworkItemReader(null,null))
                .processor(actualSettlementAmountBySaleMemberArtworkItemProcessor(null))
                .writer(actualSettlementAmountBySaleMemberArtworkItemWriter())
                .build();
    }

    @Bean
    public Step actualSettlementAmountBySaleMemberArtworksManagerStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountBySaleMemberArtworksManagerStep")
                .partitioner("actualSettlementAmountBySaleMemberArtworksStep", new SaleMemberPartitioner(saleMemberRepository))
                .step(actualSettlementAmountBySaleMemberArtworksStep())
                .partitionHandler(SaleMemberArtworksTaskExecutorPartitionHandler())
                .build();
    }

    @Bean
    public Step actualSettlementAmountByBusinessMemberArtworksStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountByBusinessMemberArtworksStep")
                .<BusinessMember, List<StreamingStatisticsDetail>>chunk(CHUNK_SIZE)
                .reader(actualSettlementAmountByBusinessMemberArtworkItemReader(null,null))
                .processor(actualSettlementAmountByBusinessMemberArtworkItemProcessor(null))
                .writer(actualSettlementAmountByBusinessMemberArtworkItemWriter())
                .build();
    }

    @Bean
    public Step actualSettlementAmountByBusinessMemberArtworksManagerStep() throws Exception {
        return this.stepBuilderFactory.get("actualSettlementAmountByBusinessMemberArtworksManagerStep")
                .partitioner("actualSettlementAmountByBusinessMemberArtworksStep", new BusinessMemberPartitioner(businessMemberRepository))
                .step(actualSettlementAmountByBusinessMemberArtworksStep())
                .partitionHandler(BusinessMemberArtworksTaskExecutorPartitionHandler())
                .build();
    }

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

    @Bean
    public Step updateViewsExcludingThisMonthOfArtworkStep() throws Exception {
        return this.stepBuilderFactory.get("updateViewsExcludingThisMonthOfArtworkStep")
                .<Artwork, Artwork>chunk(CHUNK_SIZE)
                .reader(updateViewsExcludingThisMonthOfArtworkItemReader(null,null))
                .writer(updateViewsExcludingThisMonthOfArtworkItemWriter())
                .build();
    }

    @Bean
    public Step updateViewsExcludingThisMonthOfArtworkManagerStep() throws Exception {
        return this.stepBuilderFactory.get("updateViewsExcludingThisMonthOfArtworkManagerStep")
                .partitioner("updateViewsExcludingThisMonthOfArtworkStep", new ArtworkPartitioner(artworkRepository))
                .step(updateViewsExcludingThisMonthOfArtworkStep())
                .partitionHandler(ArtworkTaskExecutorPartitionHandler())
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
        // 유, 무료로 골라서 뺀 다음에 StreamingTotal 객체 변경
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

    @Bean
    @StepScope
    public JpaPagingItemReader<? extends SaleMember> actualSettlementAmountBySaleMemberItemReader(@Value("#{stepExecutionContext[minId]}") Long minId,
                                                                                                  @Value("#{stepExecutionContext[maxId]}") Long maxId) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minId", minId);
        parameters.put("maxId", maxId);

        JpaPagingItemReader<SaleMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select s from SaleMember s where s.id between :minId and :maxId");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<? super SaleMember,? extends StreamingStatistics> actualSettlementAmountBySaleMemberItemProcessor(@Value("#{jobParameters[date]}") String date) {
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

    @Bean
    @StepScope
    public ItemWriter<? super StreamingStatistics> actualSettlementAmountBySaleMemberItemWriter() {
        return streamingStatisticsRepository::saveAll;
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<? extends BusinessMember> actualSettlementAmountByBusinessMemberItemReader(@Value("#{stepExecutionContext[minId]}") Long minId,
                                                                                                          @Value("#{stepExecutionContext[maxId]}") Long maxId) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minId", minId);
        parameters.put("maxId", maxId);

        JpaPagingItemReader<BusinessMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select b from BusinessMember b where b.id between :minId and :maxId");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<? super BusinessMember,? extends StreamingStatistics> actualSettlementAmountByBusinessMemberItemProcessor(@Value("#{jobParameters[date]}") String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date"));

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

    @Bean
    @StepScope
    public ItemWriter<? super StreamingStatistics> actualSettlementAmountByBusinessMemberItemWriter() {
        return streamingStatisticsList -> streamingStatisticsList.forEach(streamingStatisticsRepository::save);
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<? extends SaleMember> actualSettlementAmountBySaleMemberArtworkItemReader(@Value("#{stepExecutionContext[minId]}") Long minId,
                                                                                                         @Value("#{stepExecutionContext[maxId]}") Long maxId) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minId", minId);
        parameters.put("maxId", maxId);

        JpaPagingItemReader<SaleMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select s from SaleMember s where s.id between :minId and :maxId");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<? super SaleMember,? extends List<StreamingStatisticsDetail>> actualSettlementAmountBySaleMemberArtworkItemProcessor(@Value("#{jobParameters[date]}") String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date!"));

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

    @Bean
    @StepScope
    public ItemWriter<? super List<StreamingStatisticsDetail>> actualSettlementAmountBySaleMemberArtworkItemWriter() {
        return streamingStatisticsDetails -> streamingStatisticsDetails.forEach(streamingStatisticsDetailRepository::saveAll);
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<? extends BusinessMember> actualSettlementAmountByBusinessMemberArtworkItemReader(@Value("#{stepExecutionContext[minId]}") Long minId,
                                                                                                                 @Value("#{stepExecutionContext[maxId]}") Long maxId) throws Exception{
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minId", minId);
        parameters.put("maxId", maxId);

        JpaPagingItemReader<BusinessMember> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select b from BusinessMember b where b.id between :minId and :maxId");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<? super BusinessMember,? extends List<StreamingStatisticsDetail>> actualSettlementAmountByBusinessMemberArtworkItemProcessor(@Value("#{jobParameters[date]}") String date) {
        Map<String, Object> startEndMap = getStartDateTimeAndEndDateTimeOfYearMonth(date);
        LocalDateTime aggregationStart = (LocalDateTime) startEndMap.get("startDateTime");
        StreamingTotal streamingTotal = streamingTotalRepository.findByAggregationStartTime(aggregationStart)
                .orElseThrow(() -> new IllegalArgumentException("No streaming total found for input date!"));

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

    @Bean
    @StepScope
    public ItemWriter<? super List<StreamingStatisticsDetail>> actualSettlementAmountByBusinessMemberArtworkItemWriter() {
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

    @Bean
    @StepScope
    public JpaPagingItemReader<? extends Artwork> updateViewsExcludingThisMonthOfArtworkItemReader(@Value("#{stepExecutionContext[minId]}") Long minId,
                                                                                                   @Value("#{stepExecutionContext[maxId]}") Long maxId) throws Exception {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minId", minId);
        parameters.put("maxId", maxId);

        JpaPagingItemReader<Artwork> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select a from Artwork a where a.id between :minId and :maxId");
        reader.setParameterValues(parameters);
        reader.setPageSize(CHUNK_SIZE);

        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<? super Artwork> updateViewsExcludingThisMonthOfArtworkItemWriter() {
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

    @Bean
    public PartitionHandler SaleMemberTaskExecutorPartitionHandler() throws Exception {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(actualSettlementAmountBySaleMemberStep());
        handler.setTaskExecutor(this.taskExecutor);
        handler.setGridSize(10);

        return handler;
    }

    @Bean
    public PartitionHandler BusinessMemberTaskExecutorPartitionHandler() throws Exception {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(actualSettlementAmountByBusinessMemberStep());
        handler.setTaskExecutor(this.taskExecutor);
        handler.setGridSize(10);

        return handler;
    }

    @Bean
    public PartitionHandler SaleMemberArtworksTaskExecutorPartitionHandler() throws Exception {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(actualSettlementAmountBySaleMemberArtworksStep());
        handler.setTaskExecutor(this.taskExecutor);
        handler.setGridSize(10);

        return handler;
    }

    @Bean
    public PartitionHandler BusinessMemberArtworksTaskExecutorPartitionHandler() throws Exception {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(actualSettlementAmountByBusinessMemberArtworksStep());
        handler.setTaskExecutor(this.taskExecutor);
        handler.setGridSize(10);

        return handler;
    }

    private PartitionHandler ArtworkTaskExecutorPartitionHandler() throws Exception {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(updateViewsExcludingThisMonthOfArtworkStep());
        handler.setTaskExecutor(this.taskExecutor);
        handler.setGridSize(10);

        return handler;
    }
}
