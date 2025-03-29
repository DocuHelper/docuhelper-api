package org.bmserver.kafka.config

import org.bmserver.core.common.Config

enum class KafkaTopic(
    val value: String
) {
    DOCUHELPER_API("docuhelper-api"),
    DOCUHELPER_NOTICE("docuhelper-notice-" + Config.serverUuid)
}
