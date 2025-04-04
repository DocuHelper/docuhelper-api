package org.bmserver.core.chat.event

import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.domain.event.AbstractEvent
import java.util.UUID

data class ChatAnswer(
    val ask: String,
    val answer: String,
    val answerRef: List<ChatAnswerRef>,
    val document: UUID,
    val chat: UUID
) : AbstractEvent()