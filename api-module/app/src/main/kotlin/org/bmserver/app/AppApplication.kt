package org.bmserver.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@SpringBootApplication(scanBasePackages = ["org.bmserver"])
@EnableElasticsearchRepositories
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
