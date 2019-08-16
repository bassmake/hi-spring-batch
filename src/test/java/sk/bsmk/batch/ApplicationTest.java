package sk.bsmk.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.bsmk.batch.jobs.JobParameterKeys;

import javax.sql.DataSource;

import static sk.bsmk.batch.jobs.BatchConfiguration.JOB_NAME;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private BasicBatchConfigurer batchConfigurer;

  @Autowired
  private JobOperator jobOperator;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobLocator jobLocator;

  @Test
  public void test() throws Exception {

    final Job job = jobLocator.getJob(JOB_NAME);

    final JobParameters parameters = new JobParametersBuilder()
      .addString(JobParameterKeys.FILE_NAME, "sample-data.csv")
      .toJobParameters();

    final JobExecution execution = jobLauncher.run(job, parameters);

    Thread.sleep(1000);

    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }

}
