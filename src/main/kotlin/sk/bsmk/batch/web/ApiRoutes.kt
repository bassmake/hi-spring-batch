package sk.bsmk.batch.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
open class ApiRoutes(val apiHandler: ApiHandler) {

  @Bean
  open fun mainRouter() = router {
    GET("/", apiHandler::hello)
    GET("/jobs", apiHandler::jobs)
  }

}
