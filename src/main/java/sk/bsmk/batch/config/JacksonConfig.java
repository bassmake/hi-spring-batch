package sk.bsmk.batch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import sk.bsmk.batch.actuator.StepExecutionMixin;

@Configuration
public class JacksonConfig {

  @Autowired
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder
      .mixIn(StepExecution.class, StepExecutionMixin.class)
      .build();
  }
}
