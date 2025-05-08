package org.bmserver.app

import org.bmserver.core.common.ai.AiOutPort
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AppApplicationTests {
    lateinit var aiAdapter: AiOutPort
    @Test
    fun contextLoads() {
    }

    @Test
    fun test() {

        val result = aiAdapter.getEmbedding("test").block()
        println(result)
    }

}
