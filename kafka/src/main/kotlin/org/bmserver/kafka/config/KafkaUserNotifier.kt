package org.bmserver.kafka.config

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.bmserver.core.common.notice.AbstractNotice
import org.bmserver.core.common.notice.NoticeKey
import org.bmserver.core.common.notice.UserNotifier
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class KafkaUserNotifier(
    private val producer: ReactiveKafkaProducerTemplate<Any, Any>,
    ) : UserNotifier {

    override fun send(data: AbstractNotice): Mono<Void> {

        return send(data.user, data)
    }

    override fun send(user: UUID, data: Any): Mono<Void> {
        val header = mutableListOf(
            RecordHeader("noticeType", data::class.simpleName.toString().toByteArray())
        )

        val key = NoticeKey(user)

        val record = ProducerRecord<Any, Any>(
            KafkaTopic.DOCUHELPER_NOTICE.value,
            null,
            key,
            data,
            header
        )

        return producer.send(record).then()
    }
}