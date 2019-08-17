package sk.bsmk.batch.job;

import java.util.UUID;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.batches.BatchRepository;
import sk.bsmk.batch.batches.BatchState;

/**
 * Check if all rows were processed correctly at the end of processing (step) and sets state
 * properly.
 */
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

  private final BatchRepository batchRepository;

  @Autowired
  public JobCompletionListener(BatchRepository batchRepository) {
    this.batchRepository = batchRepository;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      final String batchId = jobExecution.getJobParameters().getString(JobParameterKeys.BATCH_ID);
      final BatchState batchState =
          jobExecution.getStepExecutions().stream()
                  .anyMatch(stepExecution -> stepExecution.getProcessSkipCount() > 0)
              ? BatchState.PROCESSED_WITH_FAILURES
              : BatchState.PROCESSED;
      batchRepository.updateState(UUID.fromString(batchId), batchState);
    }
  }
}
