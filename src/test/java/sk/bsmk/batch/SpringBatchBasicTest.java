package sk.bsmk.batch;

import org.junit.Test;
import org.springframework.batch.core.Job;

import static org.assertj.core.api.Assertions.assertThat;
import static sk.bsmk.batch.jobs.PointsActivationJobConfiguration.JOB_NAME;

public class SpringBatchBasicTest extends SpringBatchTest {

  @Test
  public void that_jobs_are_registered() {
    assertThat(jobRegistry.getJobNames()).isNotEmpty();
  }

  @Test
  public void that_point_activation_job_is_restartable() throws Exception {
    final Job job = jobRegistry.getJob(JOB_NAME);
    assertThat(job.isRestartable()).isTrue();
  }

}
