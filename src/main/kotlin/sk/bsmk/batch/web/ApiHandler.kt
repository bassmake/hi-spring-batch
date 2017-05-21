package sk.bsmk.batch.web

import org.springframework.batch.core.JobExecution
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
    ServerResponse.ok().syncBody("spring batch api")

  fun jobs(request: ServerRequest): Mono<ServerResponse> {
    val jobNames = jobExplorer.jobNames
    return ServerResponse.ok().syncBody(jobNames)
  }

  fun instances(request: ServerRequest): Mono<ServerResponse> {
    val jobName = request.pathVariable(ApiRoutes.JOB_NAME)
    val instances = jobExplorer.getJobInstances(jobName, 0, 1_000)
    return ServerResponse.ok().syncBody(instances)
  }

  fun executions(request: ServerRequest): Mono<ServerResponse> {
    val jobName = request.pathVariable(ApiRoutes.JOB_NAME)
    val instanceId = request.pathVariable(ApiRoutes.INSTANCE_ID).toLong()
    val instance = jobExplorer.getJobInstance(instanceId)
    val executions: List<JobExecution> = jobExplorer.getJobExecutions(instance)

    // JobExecution contains Collection<StepExecution> and StepExecution contains JobExecution
    // so we have infinite references so jackson is not able to serialize it
    val executionIds = executions.map { it -> it.id }

    return ServerResponse.ok().syncBody(executionIds)
  }

}
