package sk.bsmk.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.bsmk.batch.jobs.PointsActivationJobConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringBatchTest {

  @Autowired
  JobRegistry jobRegistry;

  @Test
  public void that_job_is_running() {
    assertThat(jobRegistry.getJobNames()).isNotEmpty();
  }

}
