package org.bmserver.gateway.chat.request

import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

class ChatSendRequest(
    val document: UUID,
    val ask: String,
) : AbstractAuthRequest()