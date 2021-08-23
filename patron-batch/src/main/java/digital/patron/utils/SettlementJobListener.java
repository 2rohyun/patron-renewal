package digital.patron.utils;

import digital.patron.domain.StreamingStatistics;
import digital.patron.repository.StreamingStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
public class SettlementJobListener implements JobExecutionListener {

    private final StreamingStatisticsRepository streamingStatisticsRepository;

    public SettlementJobListener(StreamingStatisticsRepository streamingStatisticsRepository) {
        this.streamingStatisticsRepository = streamingStatisticsRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) { }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime start = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        List<StreamingStatistics> data = streamingStatisticsRepository.findByUpdateTimeBetween(start, end);
        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        log.info(" ===================================================");
        log.info("   Patron Streaming Settlement Batch Program..");
        log.info("   Total processing time : {} millis",time);
        log.info("   Created streaming statistics data : {} cases", data.size());
        log.info(" ===================================================");

    }
}
