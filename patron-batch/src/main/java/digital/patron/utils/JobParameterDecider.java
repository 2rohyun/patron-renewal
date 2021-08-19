package digital.patron.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class JobParameterDecider implements JobExecutionDecider {

    public static final FlowExecutionStatus CONTINUE = new FlowExecutionStatus("CONTINUE");
    private final String key;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String value = jobExecution.getJobParameters().getString(key);

        if(!StringUtils.hasText(value)) {
            return FlowExecutionStatus.COMPLETED;
        }

        return CONTINUE;
    }
}
