package sk.bsmk.batch.points;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.HashMap;
import java.util.Map;

import static sk.bsmk.batch.jobs.BatchConfiguration.JOB_NAME;

@Configuration
public class PointsActivationConfig {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobLocator jobLocator;

  @Bean
  JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
    jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
    return jobRegistryBeanPostProcessor;
  }

  @Bean
  public JobDetailFactoryBean jobDetailFactoryBean() {
    JobDetailFactoryBean factory = new JobDetailFactoryBean();
    factory.setJobClass(PointsActivationJob.class);
    Map<String, Object> map = new HashMap<>();
    map.put("jobName", JOB_NAME);
    map.put("jobLauncher", jobLauncher);
    map.put("jobLocator", jobLocator);
    factory.setJobDataAsMap(map);
    factory.setGroup("etl_group");
    factory.setName("etl_job");
    return factory;
  }

  @Bean
  public SimpleTriggerFactoryBean cronTriggerFactoryBean() {
    SimpleTriggerFactoryBean stFactory = new SimpleTriggerFactoryBean();
    stFactory.setJobDetail(jobDetailFactoryBean().getObject());
    stFactory.setStartDelay(200);
    stFactory.setName("cron_trigger");
    stFactory.setGroup("cron_group");
    stFactory.setRepeatInterval(500);
    return stFactory;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
    scheduler.setTriggers(cronTriggerFactoryBean().getObject());
    return scheduler;
  }

}
