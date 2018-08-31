package sk.bsmk.batch.actuator;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = BatchJobsInfoEndpoint.BATCH_JOBS_INFO)
public class BatchJobsInfoEndpoint {

  public static final String BATCH_JOBS_INFO = "batch-jobs";

  private final JobExplorer jobExplorer;

  @Autowired
  public BatchJobsInfoEndpoint(JobExplorer jobExplorer) {
    this.jobExplorer = jobExplorer;
  }

  @ReadOperation
  public String batchJobsInfo() {
    return String.join("|", jobExplorer.getJobNames());
  }

}
