package org.bmserver.app.test

import org.bmserver.gateway.config.security.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class TemplateTest {
    init {
        println("TemplateTest")
    }

    @GetMapping
    fun index(): String = "index"
}

@RestController
@RequestMapping("/user")
class RestApiTest {
    @GetMapping("/info")
    fun getUserInfo(
        @AuthenticationPrincipal user: User,
    ) = user
}
