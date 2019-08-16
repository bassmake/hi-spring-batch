package sk.bsmk.batch;

import static org.assertj.core.api.Assertions.*;
import static sk.bsmk.batch.jobs.BatchConfiguration.JOB_NAME;

import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sk.bsmk.batch.batches.Batch;
import sk.bsmk.batch.batches.BatchRepository;
import sk.bsmk.batch.batches.BatchState;
import sk.bsmk.batch.batches.ImmutableBatch;
import sk.bsmk.batch.jobs.JobParameterKeys;
import sk.bsmk.batch.person.Person;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {

  @Autowired private DataSource dataSource;

  @Autowired private BasicBatchConfigurer batchConfigurer;

  @Autowired private JobOperator jobOperator;

  @Autowired private JobLauncher jobLauncher;

  @Autowired private JobLocator jobLocator;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired BatchRepository batchRepository;

  @Test
  public void test() throws Exception {

    final JobExecution executionA = storeBatchAndRun("file-a.csv");
    final JobExecution executionB = storeBatchAndRun("file-b.csv");

    Thread.sleep(1000);

    assertThat(executionA.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(executionB.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    final List<Person> persons = fetchPersons();
    assertThat(persons).hasSize(10);
  }

  private JobExecution storeBatchAndRun(String fileName) throws Exception {

    final Batch batch =
        ImmutableBatch.builder()
            .id(UUID.randomUUID())
            .fileName(fileName)
            .state(BatchState.NEW)
            .build();

    batchRepository.store(batch);

    final Job job = jobLocator.getJob(JOB_NAME);
    return jobLauncher.run(
        job,
        new JobParametersBuilder()
            .addString(JobParameterKeys.FILE_NAME, fileName)
            .addString(JobParameterKeys.BATCH_ID, batch.id().toString())
            .toJobParameters());
  }

  private List<Person> fetchPersons() {
    return jdbcTemplate.query(
        "SELECT first_name, last_name, points FROM people",
        (rs, row) -> new Person(rs.getString(1), rs.getString(2), rs.getInt(3)));
  }
}
