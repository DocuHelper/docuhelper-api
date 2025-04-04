package org.bmserver.core.chat.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

class ChatAnswerRef(
    val chat: UUID,
    val chunk: UUID,
    val similarity: Float
):BaseDomain()