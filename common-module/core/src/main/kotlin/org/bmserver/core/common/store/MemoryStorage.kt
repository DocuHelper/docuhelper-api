package org.bmserver.core.common.store

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MemoryStorage {
    fun setValue(key: Any, value: Any): Mono<Void>
    fun deleteValue(key: Any): Mono<Void>

    fun setHash(key: Any, hashKey: Any, value: Any): Mono<Void>
    fun deleteHash(key: Any): Mono<Void>
    fun deleteHashValue(key: Any, hashKey: Any): Mono<Void>

    fun incrementHash(key: Any, hashKey: Any, delta: Double): Mono<Double>
    fun incrementHash(key: Any, hashKey: Any, delta: Long): Mono<Long>

    fun <T> getHashHashKeys(key: Any, hashKeyType: Class<T>): Flux<T>
    fun <HK, V> getHash(key: Any, hashKeyType: Class<HK>, valueType: Class<V>): Flux<Map.Entry<HK, V>>

}