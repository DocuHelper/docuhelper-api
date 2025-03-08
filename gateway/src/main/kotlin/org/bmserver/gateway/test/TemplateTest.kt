package org.bmserver.app.test

import org.bmserver.core.document.model.Document
import org.bmserver.core.document.model.DocumentState
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@Controller
class TemplateTest {
    init {
        println("TemplateTest")
    }

    @GetMapping
    fun index(): String = "index"
}

@RestController
@RequestMapping("/test")
class RestApiTest {
    @GetMapping
    fun test() =
        Mono.just(
            Document(
                name = "dd",
                file = UUID.randomUUID(),
                owner = UUID.randomUUID(),
                state = DocumentState.PARSING,
            ),
        )
}
