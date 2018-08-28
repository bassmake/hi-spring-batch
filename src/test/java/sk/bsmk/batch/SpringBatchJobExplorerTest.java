package sk.bsmk.batch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sk.bsmk.batch.points.PointsService;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBatchJobExplorerTest extends SpringBatchTest {

  @Autowired
  PointsService pointsService;

  // disabled via spring.batch.job.enabled: false
  @Test
  public void that_job_is_not_available_via_job_explorer() {
    assertThat(jobExplorer.getJobNames()).isEmpty();
    assertThat(pointsService.getNumberOfActivations()).isZero();
  }

}
