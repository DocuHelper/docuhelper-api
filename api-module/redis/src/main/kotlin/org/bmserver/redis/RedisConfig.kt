package org.bmserver.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.redis.dto.Server
import org.bmserver.redis.dto.User
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun userRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<User, Int> {

        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        val keySerializer: RedisSerializer<User> = Jackson2JsonRedisSerializer(objectMapper, User::class.java)
        val hashKeySerializer: RedisSerializer<Server> = Jackson2JsonRedisSerializer(objectMapper, Server::class.java)
        val valueSerializer: RedisSerializer<Int> = Jackson2JsonRedisSerializer(objectMapper, Int::class.java)

        val context = RedisSerializationContext
            .newSerializationContext<User, Int>(jsonSerializer)
            .key(keySerializer)
            .hashKey(hashKeySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun serverRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<Server, Int> {

        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        val keySerializer: RedisSerializer<Server> = Jackson2JsonRedisSerializer(objectMapper, Server::class.java)
        val hashKeySerializer: RedisSerializer<User> = Jackson2JsonRedisSerializer(objectMapper, User::class.java)
        val valueSerializer: RedisSerializer<Int> = Jackson2JsonRedisSerializer(objectMapper, Int::class.java)

        val context = RedisSerializationContext
            .newSerializationContext<Server, Int>(jsonSerializer)
            .key(keySerializer)
            .hashKey(hashKeySerializer)     // User
            .hashValue(valueSerializer)   // Int
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
}