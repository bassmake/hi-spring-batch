package sk.bsmk.batch.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
open class ApiRoutes(val apiHandler: ApiHandler) {

  companion object {
    const val JOB_NAME = "jobName"
    const val INSTANCE_ID = "instanceId"
  }

  @Bean
  open fun mainRouter() = router {
    GET("/", apiHandler::hello)
    GET("/jobs", apiHandler::jobs)
    GET("/jobs/{$JOB_NAME}/instances", apiHandler::instances)
    GET("/jobs/{$JOB_NAME}/instances/{$INSTANCE_ID}/executions", apiHandler::executions)
  }

}
