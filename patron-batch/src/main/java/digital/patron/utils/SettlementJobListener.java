package digital.patron.utils;

import digital.patron.repository.MonthSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class SettlementJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) { }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        log.info(" ===============================================");
        log.info("|| Patron Streaming Settlement Batch Program.. ||");
        log.info("|| Total processing time : {} millis        ||",time);
        log.info(" ===============================================");

    }
}
