package org.bmserver.kafka.config

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.bmserver.core.common.notice.NoticeKey
import org.bmserver.core.common.notice.SubScriptionNotifier
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.UUID

@Component
class KafkaSubScriptionNotifier(
    private val reactiveKafkaNotifierConsumerTemplate: ReactiveKafkaConsumerTemplate<NoticeKey, LinkedHashMap<String, String>>
) : SubScriptionNotifier, ApplicationRunner {
    private var sink =
        Sinks.many().multicast().onBackpressureBuffer<ConsumerRecord<NoticeKey, LinkedHashMap<String, String>>>()

    override fun sub(user: UUID): Flux<Pair<String, LinkedHashMap<String, String>>> {
        return sink.asFlux()
            .publish()
            .autoConnect(0)
            .filter { it.key().user == user }
            .map {
                val noticeType = it.headers().find { it.key() == "noticeType" }?.value()?.toString(Charsets.UTF_8) ?: ""
                it.value().put("noticeType", noticeType)
                Pair(noticeType, it.value())
            }
    }

    override fun run(args: ApplicationArguments?) {
        reactiveKafkaNotifierConsumerTemplate.receiveAutoAck()
            .subscribe { sink.tryEmitNext(it) }

    }
}