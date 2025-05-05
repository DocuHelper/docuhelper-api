package org.bmserver.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun commonRedisTemplate(factory: ReactiveRedisConnectionFactory, objectMapper: ObjectMapper): ReactiveRedisTemplate<Any, Any> {

        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        val keySerializer: RedisSerializer<Any> = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)
        val hashKeySerializer: RedisSerializer<Any> = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)
        val valueSerializer: RedisSerializer<Any> = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)

        val context = RedisSerializationContext
            .newSerializationContext<Any, Any>(jsonSerializer)
            .key(keySerializer)
            .hashKey(hashKeySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, context)
    }

}
