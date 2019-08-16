package sk.bsmk.batch.jobs;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import sk.bsmk.batch.batches.BatchRepository;
import sk.bsmk.batch.batches.RawRow;

/**
 * I was not able to access stepExecution via {@link
 * org.springframework.batch.core.listener.SkipListenerSupport}.
 */
public class RowSkipListener {

  private static final Logger log = LoggerFactory.getLogger(RowSkipListener.class);

  private StepExecution stepExecution;

  private final BatchRepository batchRepository;

  public RowSkipListener(BatchRepository batchRepository) {
    this.batchRepository = batchRepository;
  }

  @OnSkipInProcess
  public void skip(RawRow item, Throwable t) {
    final String batchId = stepExecution.getJobParameters().getString(JobParameterKeys.BATCH_ID);
    log.warn("Skipping row in batch {}: {}", batchId, item);
    batchRepository.addUnprocessedRow(UUID.fromString(batchId), item.lineNumber());
  }

  @BeforeStep
  public void setStepExecution(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }
}
