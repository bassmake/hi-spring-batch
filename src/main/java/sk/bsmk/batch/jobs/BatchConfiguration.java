package sk.bsmk.batch.jobs;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sk.bsmk.batch.person.Person;
import sk.bsmk.batch.person.PersonItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  public static final String JOB_NAME = "importUserJob";

  @Autowired public JobBuilderFactory jobBuilderFactory;

  @Autowired public StepBuilderFactory stepBuilderFactory;

  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
    jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
    return jobRegistryBeanPostProcessor;
  }

  @Bean
  @JobScope
  public FlatFileItemReader<Person> reader(
      @Value(JobParameterKeys.FILE_NAME_PARAM) String fileName) {
    return new FlatFileItemReaderBuilder<Person>()
        .name("personItemReader")
        .resource(new ClassPathResource(fileName))
        .delimited()
        .names(new String[] {"firstName", "lastName", "points"})
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<Person>() {
              {
                setTargetType(Person.class);
              }
            })
        .build();
  }

  @Bean
  public PersonItemProcessor processor() {
    return new PersonItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql(
            "INSERT INTO people (first_name, last_name, points) VALUES (:firstName, :lastName, :points)")
        .dataSource(dataSource)
        .build();
  }

  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(step1)
        .end()
        .build();
  }

  @Bean
  public Step step1(ItemReader<Person> reader, JdbcBatchItemWriter<Person> writer) {
    return stepBuilderFactory
        .get("step1")
        .<Person, Person>chunk(10)
        .reader(reader)
        .processor(processor())
        .writer(writer)
        .build();
  }
}
