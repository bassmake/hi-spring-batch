package sk.bsmk.batch.jobs;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class JobsSchedulingConfiguration {

  public static final long INITIAL_DELAY = 500L;

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  JobOperator jobOperator;

  @Autowired
  Job pointsActivationJob;

  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }

  @Bean
  public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider) {
    return ScheduledLockConfigurationBuilder
      .withLockProvider(lockProvider)
      .withPoolSize(10)
      .withDefaultLockAtMostFor(Duration.ofMinutes(10))
      .build();
  }

  @Scheduled(fixedRate = 1_000, initialDelay = INITIAL_DELAY)
  @SchedulerLock(name = PointsActivationJobConfiguration.JOB_NAME)
  public void runPointsActivationJob() throws Exception {
    final JobParameter createdAt = new JobParameter(new Date());
    final Map<String, JobParameter> params = new HashMap<>();
    params.put("createdAt", createdAt);
    final JobParameters jobParameters = new JobParameters(params);
    jobLauncher.run(pointsActivationJob, jobParameters);
  }

}
