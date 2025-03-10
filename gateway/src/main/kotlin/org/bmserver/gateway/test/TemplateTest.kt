package org.bmserver.app.test

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TemplateTest {
    init {
        println("TemplateTest")
    }

    @GetMapping
    fun index(): String = "index"
}
