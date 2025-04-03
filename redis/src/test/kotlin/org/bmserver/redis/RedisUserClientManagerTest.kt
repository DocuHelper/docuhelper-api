package org.bmserver.redis

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class RedisUserClientManagerTest {
    @Autowired
    private lateinit var redisUserClientManager: RedisUserClientManager

    @Test
    fun test() {
        redisUserClientManager.getUserClientInfo(
            UUID.fromString("c6751ab8-5fb5-44f7-8ab3-b326686b6640")
        )
    }
}