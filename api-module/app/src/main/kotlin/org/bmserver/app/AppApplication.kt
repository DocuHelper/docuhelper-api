package org.bmserver.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.bmserver"])
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
