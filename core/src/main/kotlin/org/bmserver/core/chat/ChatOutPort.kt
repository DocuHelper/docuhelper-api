package org.bmserver.core.chat

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.CommonDomainService

interface ChatOutPort : CommonDomainService<Chat> {
}