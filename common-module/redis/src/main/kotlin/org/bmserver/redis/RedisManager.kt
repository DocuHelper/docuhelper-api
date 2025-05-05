package org.bmserver.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.core.common.store.MemoryStorage
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class RedisManager(
    private val commonRedisTemplate: ReactiveRedisTemplate<Any, Any>,
    private val objectMapper: ObjectMapper
) : MemoryStorage {
    override fun setValue(key: Any, value: Any): Mono<Void> =
        commonRedisTemplate.opsForValue()
            .set(key, value)
            .then()

    override fun deleteValue(key: Any): Mono<Void> =
        commonRedisTemplate.opsForValue()
            .delete(key)
            .then()

    override fun setHash(key: Any, hashKey: Any, value: Any): Mono<Void> =
        commonRedisTemplate.opsForHash<Any, Any>()
            .put(key, hashKey, value)
            .then()

    override fun deleteHashValue(key: Any, hashKey: Any): Mono<Void> =
        commonRedisTemplate.opsForHash<Any, Any>()
            .remove(key, hashKey)
            .then()

    override fun deleteHash(key: Any): Mono<Void> =
        commonRedisTemplate.opsForHash<Any, Any>()
            .delete(key)
            .then()

    override fun incrementHash(key: Any, hashKey: Any, delta: Double): Mono<Double> =
        commonRedisTemplate.opsForHash<Any, Any>()
            .increment(key, hashKey, delta)

    override fun incrementHash(key: Any, hashKey: Any, delta: Long): Mono<Long> =
        commonRedisTemplate.opsForHash<Any, Any>()
            .increment(key, hashKey, delta)

    override fun <T> getHashHashKeys(key: Any): Flux<T> =
        commonRedisTemplate.opsForHash<T, Any>().keys(key)

    override fun <HK, V> getHash(key: Any): Flux<Map.Entry<HK, V>> {
        return commonRedisTemplate.opsForHash<HK, V>()
            .entries(key)
    }


}