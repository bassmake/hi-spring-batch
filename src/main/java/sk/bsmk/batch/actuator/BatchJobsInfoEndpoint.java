package sk.bsmk.batch.actuator;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.List;

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
  public List<String> jobNames() {
    return jobExplorer.getJobNames();
  }

  @ReadOperation
  public List<JobInstance> jobInstances(@Selector String arg0) {
    return jobExplorer.getJobInstances(arg0, 0, 10);
  }

  @ReadOperation
  public JobExecution jobExecution(@Selector String arg0, @Selector Long arg1) {
    return jobExplorer.getJobExecution(arg1);
  }

  @ReadOperation
  public StepExecution stepExecution(@Selector String arg0, @Selector Long arg1, @Selector Long arg2) {
    return jobExplorer.getStepExecution(arg1, arg2);
  }

}
