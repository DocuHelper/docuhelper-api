package org.bmserver.gateway.config.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.util.UUID


@Configuration
class BasicRedisConfig {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    @Qualifier
    fun redisOperationsChatClient(factory: ReactiveRedisConnectionFactory?): ReactiveRedisOperations<ChatClientKey, ChatClientValue> {
        val keySerializer: RedisSerializer<ChatClientKey> = Jackson2JsonRedisSerializer(objectMapper, ChatClientKey::class.java)
        val valueSerializer: RedisSerializer<ChatClientValue> = Jackson2JsonRedisSerializer(objectMapper, ChatClientValue::class.java)

        val serializationContext = RedisSerializationContext
            .newSerializationContext<ChatClientKey, ChatClientValue>()
            .key(keySerializer)
            .value(valueSerializer)
            .hashKey(keySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory!!, serializationContext)
    }
}

class ChatClientKey(
    val user: UUID
) {
    override fun hashCode(): Int {
        return user.hashCode()
    }
}

data class ChatClientValue(
    val clients: MutableMap<UUID,Int> = mutableMapOf()
)
