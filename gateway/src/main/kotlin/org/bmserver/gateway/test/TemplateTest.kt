package org.bmserver.app.test

import org.bmserver.gateway.config.security.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.ReactiveSecurityContextHolder
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
    @GetMapping
    fun getCurrentUser() =
        ReactiveSecurityContextHolder
            .getContext()
            .map { securityContext ->
                securityContext.authentication.principal as User
            }

    @GetMapping("/info")
    fun getUserInfo(
        @AuthenticationPrincipal user: User,
    ) = user
}
