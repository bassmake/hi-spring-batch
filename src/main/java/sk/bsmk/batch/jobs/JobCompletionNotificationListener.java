package sk.bsmk.batch.jobs;

import java.util.UUID;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.batches.BatchRepository;
import sk.bsmk.batch.batches.BatchState;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private final BatchRepository batchRepository;

  @Autowired
  public JobCompletionNotificationListener(BatchRepository batchRepository) {
    this.batchRepository = batchRepository;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      final String batchId = jobExecution.getJobParameters().getString(JobParameterKeys.BATCH_ID);
      batchRepository.updateState(UUID.fromString(batchId), BatchState.PROCESSED);
    }
  }
}
