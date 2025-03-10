package org.bmserver.app.test

import org.bmserver.gateway.config.security.SecurityUtil
import org.bmserver.gateway.config.security.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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
    @PostMapping
    fun getCurrentUser(
        @RequestBody request: Mono<String?>,
    ) = request.flatMap {
        SecurityUtil
            .getRequestUser()
            .doOnNext { println(it) }
    }

    @GetMapping("/info")
    fun getUserInfo(
        @AuthenticationPrincipal user: User,
    ) = user
}
