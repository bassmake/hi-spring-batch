package sk.bsmk.batch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BatchApplication

fun main(args: Array<String>): Unit {
  SpringApplication.run(BatchApplication::class.java, *args)
}
