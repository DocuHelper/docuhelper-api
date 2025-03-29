package org.bmserver.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.core.common.notice.ClientKey
import org.bmserver.core.common.notice.ClientValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
class RedisConfig {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun redisOperationsClient(factory: ReactiveRedisConnectionFactory?): ReactiveRedisOperations<ClientKey, ClientValue> {
        val keySerializer: RedisSerializer<ClientKey> = Jackson2JsonRedisSerializer(objectMapper, ClientKey::class.java)
        val valueSerializer: RedisSerializer<ClientValue> = Jackson2JsonRedisSerializer(objectMapper, ClientValue::class.java)

        val serializationContext = RedisSerializationContext
            .newSerializationContext<ClientKey, ClientValue>()
            .key(keySerializer)
            .value(valueSerializer)
            .hashKey(keySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory!!, serializationContext)
    }
}