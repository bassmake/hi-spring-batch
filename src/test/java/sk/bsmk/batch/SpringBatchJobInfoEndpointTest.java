package sk.bsmk.batch;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import sk.bsmk.batch.actuator.BatchJobsInfoEndpoint;
import sk.bsmk.batch.jobs.PointsActivationJobConfiguration;

import static sk.bsmk.batch.jobs.PointsActivationJobConfiguration.JOB_NAME;

public class SpringBatchJobInfoEndpointTest extends SpringBatchTest {

  @LocalServerPort int port;

  @Test
  public void that_batch_jobs_are_available() {

    given()
      .when()
      .get("actuator/" + BatchJobsInfoEndpoint.BATCH_JOBS_INFO)
      .then()
      .log().all()
      .statusCode(HttpStatus.OK.value())
      .body(Matchers.containsString(JOB_NAME));

  }

  @Test
  public void that_batch_job_executions_are_available() {

    given()
      .when()
      .get("actuator/{path}/{name}", BatchJobsInfoEndpoint.BATCH_JOBS_INFO, JOB_NAME)
      .then()
      .log().all()
      .statusCode(HttpStatus.OK.value())
      .body(Matchers.containsString(JOB_NAME))
      .body(Matchers.containsString("\"instanceId\""))
      .body(Matchers.containsString("\"id\""));

  }

  @Test
  public void that_batch_job_execution_is_available() {

    given()
      .when()
      .get("actuator/{path}/{name}/{executionId}", BatchJobsInfoEndpoint.BATCH_JOBS_INFO, JOB_NAME, 0)
      .then()
      .log().all()
      .statusCode(HttpStatus.OK.value());

  }

  @Test
  public void that_batch_step_execution_is_available() {

    given()
      .when()
      .get("actuator/{path}/{name}/{executionId}/{stepId}", BatchJobsInfoEndpoint.BATCH_JOBS_INFO, JOB_NAME, 0, 0)
      .then()
      .log().all()
      .statusCode(HttpStatus.OK.value());

  }

  @Test
  public void that_scheduled_tasks_info_is_available() {

    given()
      .when()
      .get("actuator/scheduledtasks")
      .then()
      .log().all()
    .statusCode(HttpStatus.OK.value());

  }

  private RequestSpecification given() {
    return RestAssured.given()
      .log().all()
      .port(port);
  }

}
