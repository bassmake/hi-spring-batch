package sk.bsmk.batch.web

import org.springframework.batch.core.explore.JobExplorer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ApiHandler(
  val jobExplorer: JobExplorer
) {

  fun hello(request: ServerRequest): Mono<ServerResponse> =
    ServerResponse.ok().syncBody("Hello")

  fun jobs(request: ServerRequest): Mono<ServerResponse> {
    val jobNames = jobExplorer.jobNames
    return ServerResponse.ok().syncBody(jobNames)
  }

}
