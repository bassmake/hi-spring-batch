package sk.bsmk.batch.actuator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.batch.core.JobExecution;

public interface StepExecutionMixin {

  @JsonIgnore
  JobExecution getJobExecution();

}
