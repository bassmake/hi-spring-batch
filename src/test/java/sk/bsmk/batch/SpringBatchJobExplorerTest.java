package sk.bsmk.batch;

import org.junit.Test;
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

  @Autowired
  PointsService pointsService;

  // disabled via spring.batch.job.enabled: false
  @Test
  public void that_job_is_not_available_via_job_explorer() {
    assertThat(jobExplorer.getJobNames()).isEmpty();
    assertThat(pointsService.getNumberOfActivations()).isZero();
  }

  @Test
  public void that_job_is_available_via_job_explorer_after_run() throws Exception {
    Thread.sleep(INITIAL_DELAY + 2_000);
    assertThat(jobExplorer.getJobNames()).isNotEmpty();
    assertThat(pointsService.getNumberOfActivations()).isGreaterThan(0);

    final JobInstance lastJobInstance = jobExplorer.getJobInstances(PointsActivationJobConfiguration.JOB_NAME, 0, 1).get(0);
    final List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(lastJobInstance);

    assertThat(jobExecutions.size()).isGreaterThanOrEqualTo(2);

    {
      final JobExecution first = jobExplorer.getJobExecution(jobExecutions.get(0).getId());
      final StepExecution stepExecution = stepExecution(first.getStepExecutions());

    }

    {
      final JobExecution second = jobExplorer.getJobExecution(jobExecutions.get(1).getId());
      final StepExecution stepExecution = stepExecution(second.getStepExecutions());

    }

  }

  private static StepExecution stepExecution(Collection<StepExecution> executions) {
    assertThat(executions).hasSize(1);
    return executions.stream().findFirst().orElseThrow(() -> new RuntimeException("No step execution found"));
  }

}
