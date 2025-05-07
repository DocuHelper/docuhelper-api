package org.bmserver.`user-notice`.config

import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.NoticeKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaConfig {

    /**
     * 채팅/유저 알림 수신
     */
    @Bean
    @Qualifier
    fun reactiveKafkaNotifierConsumerTemplate(props: KafkaProperties): ReactiveKafkaConsumerTemplate<NoticeKey, LinkedHashMap<String, String>> {
        val consumerProperties = props.buildConsumerProperties()
        consumerProperties.putAll(
            mapOf(
                "group.id" to "docuhelper-api",
                "spring.json.trusted.packages" to "*",
                "spring.json.use.type.headers" to false,
                "spring.json.key.default.type" to "org.bmserver.core.common.notice.NoticeKey",
                "spring.json.value.default.type" to "java.util.LinkedHashMap",
                "key.deserializer" to "org.springframework.kafka.support.serializer.JsonDeserializer",
                "value.deserializer" to "org.springframework.kafka.support.serializer.JsonDeserializer",
            )
        )

        val opt = ReceiverOptions.create<NoticeKey, LinkedHashMap<String, String>>(consumerProperties)
            .subscription(
                listOf(
                    "${KafkaTopic.DOCUHELPER_NOTICE.value}-${Config.serverUuid}"
                )
            )

        return ReactiveKafkaConsumerTemplate(opt)
    }

}
