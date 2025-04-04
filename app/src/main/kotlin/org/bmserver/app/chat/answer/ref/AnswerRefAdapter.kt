package org.bmserver.app.chat.answer.ref

import org.bmserver.core.chat.answer.ref.AnswerRefOutPort
import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.common.domain.BaseDomainRepository
import org.bmserver.core.common.domain.BaseDomainService
import org.springframework.stereotype.Component

@Component
class AnswerRefAdapter(
    private val baseDomainRepository: BaseDomainRepository<ChatAnswerRef>,
    private val baseDomainQueryRepository: BaseDomainQueryRepository<ChatAnswerRef>
) : AnswerRefOutPort, BaseDomainService<ChatAnswerRef>(
    baseDomainRepository,
    baseDomainQueryRepository
) {
}