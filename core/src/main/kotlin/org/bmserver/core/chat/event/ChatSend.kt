package org.bmserver.core.chat.event

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.event.AbstractEvent

class ChatSend(
    val chat: Chat
): AbstractEvent() {
}