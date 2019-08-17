package sk.bsmk.batch;

import static org.assertj.core.api.Assertions.*;
import static sk.bsmk.batch.job.BatchJobConfiguration.JOB_NAME;

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
import sk.bsmk.batch.job.JobParameterKeys;
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

  @Autowired private BatchRepository batchRepository;

  @Test
  public void test() throws Exception {

    final UUID batchIdA = UUID.randomUUID();
    final UUID batchIdB = UUID.randomUUID();

    final JobExecution executionA = storeBatchAndRun(batchIdA, "file-a.csv");
    final JobExecution executionB = storeBatchAndRun(batchIdB, "file-b.csv");

    Thread.sleep(1000);

    assertThat(executionA.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(batchRepository.get(batchIdA).state()).isEqualTo(BatchState.PROCESSED_WITH_FAILURES);
    assertThat(batchRepository.getUnprocessedRows(batchIdA)).containsExactly(2);

    assertThat(executionB.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(batchRepository.get(batchIdB).state()).isEqualTo(BatchState.PROCESSED);
    assertThat(batchRepository.getUnprocessedRows(batchIdB)).isEmpty();

    final List<Person> persons = fetchPersons();
    assertThat(persons).hasSize(9);

    assertThat(persons).allMatch(person -> person.getPoints() == 1);
  }

  private JobExecution storeBatchAndRun(UUID batchId, String fileName) throws Exception {

    final Batch batch =
        ImmutableBatch.builder().id(batchId).fileName(fileName).state(BatchState.NEW).build();

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
