package org.bmserver.app.chat.answer.ref

import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.domain.BaseDomainRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatAnswerRefRepository :BaseDomainRepository<ChatAnswerRef> {
}