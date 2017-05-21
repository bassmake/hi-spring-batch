package sk.bsmk.batch.web

import org.springframework.batch.core.repository.dao.JobExecutionDao
import org.springframework.batch.core.repository.dao.JobInstanceDao
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ApiHandler(
  val jobInstanceDao: JobInstanceDao
) {

  fun hello(request: ServerRequest): Mono<ServerResponse> =
    ServerResponse.ok().syncBody("Hello")

  fun jobs(request: ServerRequest): Mono<ServerResponse> {
    val jobNames = jobInstanceDao.jobNames
    return ServerResponse.ok().syncBody(jobNames)
  }

}
