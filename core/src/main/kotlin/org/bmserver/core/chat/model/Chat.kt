package org.bmserver.core.chat.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

class Chat(
    val document: UUID,
    val userUuid: UUID,
    val userAsk: String,
    var state: ChatState = ChatState.PROCESSING,
    var result: String? = null,
) : BaseDomain()