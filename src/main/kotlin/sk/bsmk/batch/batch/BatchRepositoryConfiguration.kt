package sk.bsmk.batch.batch

import org.springframework.batch.core.repository.dao.JobInstanceDao
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BatchRepositoryConfiguration {

  @Bean
  open fun jobRepositoryFactoryBean() = MapJobRepositoryFactoryBean()

  @Bean
  open fun jobInstanceDao(jobRepositoryFactoryBean: MapJobRepositoryFactoryBean): JobInstanceDao {
    return jobRepositoryFactoryBean.jobInstanceDao
  }


}
