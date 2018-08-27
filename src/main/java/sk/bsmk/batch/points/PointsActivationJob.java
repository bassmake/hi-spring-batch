package sk.bsmk.batch.points;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class PointsActivationJob extends QuartzJobBean {

  private static final Logger log = LoggerFactory.getLogger(PointsActivationJob.class);
  private String jobName;
  private JobLauncher jobLauncher;
  private JobLocator jobLocator;

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public void setJobLauncher(JobLauncher jobLauncher) {
    this.jobLauncher = jobLauncher;
  }

  public void setJobLocator(JobLocator jobLocator) {
    this.jobLocator = jobLocator;
  }

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    try {

      final Job job = jobLocator.getJob(jobName);
      final JobExecution jobExecution = jobLauncher.run(job, new JobParameters());

      log.info("{}_{} was completed successfully", job.getName(), jobExecution.getId());

    } catch (Exception e) {
      throw new JobExecutionException(e);
    }
  }

}
