package sk.bsmk.batch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import sk.bsmk.batch.jobs.PointsActivationJobConfiguration;
import sk.bsmk.batch.points.PointsService;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sk.bsmk.batch.jobs.JobsSchedulingConfiguration.INITIAL_DELAY;

public class SpringBatchJobExplorerTest extends SpringBatchTest {

  private static final Logger log = LoggerFactory.getLogger(SpringBatchJobExplorerTest.class);

  @Autowired
  PointsService pointsService;

  // disabled via spring.batch.job.enabled: false
  @Test
  public void that_job_is_not_available_via_job_explorer() {
    assertThat(jobExplorer.getJobNames()).isEmpty();
    assertThat(pointsService.numberOfActivations()).isZero();
  }

  @Test
  public void that_job_is_available_via_job_explorer_after_run() throws Exception {
    Thread.sleep(INITIAL_DELAY + 2_000);
    assertThat(jobExplorer.getJobNames()).isNotEmpty();
    assertThat(pointsService.numberOfActivations()).isGreaterThan(0);

    final List<JobInstance> jobInstances = jobExplorer.getJobInstances(PointsActivationJobConfiguration.JOB_NAME, 0, 10);

    assertThat(jobInstances).hasSize(pointsService.generations().size());

    // we want to get first run first, and jobInstance are ordered the other way (from latest)
    for (int i = 1; i <= jobInstances.size(); i++) {
      validate(jobInstances.get(jobInstances.size() - i), i);
    }


  }

  private void validate(JobInstance jobInstance, int index) {
    log.info("Validating {}: {}", index, jobInstance);
    final List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);

    assertThat(jobExecutions).hasSize(1);

    final JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutions.get(0).getId());
    final StepExecution stepExecution = stepExecution(jobExecution);
    final int count = pointsService.generations().get(index);

    assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    assertThat(stepExecution.getReadCount()).isEqualTo(count);
    assertThat(stepExecution.getWriteCount()).isEqualTo(count / 2);
    assertThat(stepExecution.getCommitCount()).isEqualTo(count + 1);

    assertThat(stepExecution.getRollbackCount()).isEqualTo(0);
    assertThat(stepExecution.getReadSkipCount()).isEqualTo(0);
    assertThat(stepExecution.getProcessSkipCount()).isEqualTo(0);
    assertThat(stepExecution.getWriteSkipCount()).isEqualTo(0);
    assertThat(stepExecution.getFilterCount()).isEqualTo(count / 2 + count % 2);
    assertThat(stepExecution.getFailureExceptions()).isEmpty();
  }

  private StepExecution stepExecution(JobExecution execution) {
    final Collection<StepExecution> executions = execution.getStepExecutions();
     assertThat(executions).hasSize(1);
    return executions.stream().findFirst().orElseThrow(() -> new RuntimeException("No step execution found"));
  }

}
