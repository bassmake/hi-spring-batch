package sk.bsmk.batch;

import io.restassured.RestAssured;
import org.junit.Test;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import sk.bsmk.batch.actuator.BatchJobsInfoEndpoint;

public class SpringBatchJobInfoTest extends SpringBatchTest {

  @LocalServerPort int port;

  @Test
  public void that_batch_jobs_info_is_available() {

    RestAssured.given()
      .port(port)
      .when()
      .get("actuator/" + BatchJobsInfoEndpoint.BATCH_JOBS_INFO)
      .then()
      .log().all()
    .statusCode(HttpStatus.OK.value());

  }


  @Test
  public void that_scheduled_tasks_info_is_available() {

    RestAssured.given()
      .port(port)
      .when()
      .get("actuator/scheduledtasks")
      .then()
      .log().all()
    .statusCode(HttpStatus.OK.value());

  }

}
