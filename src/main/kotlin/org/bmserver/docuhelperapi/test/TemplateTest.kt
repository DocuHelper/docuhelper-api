package org.bmserver.docuhelperapi.test

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TemplateTest {
    @GetMapping
    fun index(): String = "index"
}
