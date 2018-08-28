package sk.bsmk.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.bsmk.batch.points.Points;

@Configuration
public class PointsActivationJobConfiguration {

  public static final String JOB_NAME = "points_activation_job";

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job pointsActivationJob(
    JobCompletionNotificationListener listener,
    Step pointsActivationStep
  ) {
    return jobBuilderFactory.get(JOB_NAME)
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .flow(pointsActivationStep)
      .end()
      .build();
  }

  @Bean
  public Step pointsActivationStep(
    PointsActivationReader reader,
    PointsActivationProcessor processor,
    PointsActivationWriter writer
  ) {
    return stepBuilderFactory.get("points_activation_step")
      .<Points, Points>chunk(1)
      .reader(reader)
      .processor(processor)
//      .writer(writer)
      .build();
  }

}
