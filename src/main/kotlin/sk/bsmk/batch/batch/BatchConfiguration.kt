package sk.bsmk.batch.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.dao.MapJobInstanceDao
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
open class BatchConfiguration(
  val jobBuilderFactory: JobBuilderFactory,
  val stepBuilderFactory: StepBuilderFactory
) {

  @Bean
  open fun step1(): Step =
    stepBuilderFactory.get("step1")
      .tasklet({ _, _ -> null })
      .build()

  @Bean
  open fun job(step1: Step): Job =
    jobBuilderFactory.get("job1")
      .incrementer(RunIdIncrementer())
      .start(step1)
      .build()

}

