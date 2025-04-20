package org.bmserver.core.chat.answer.ref

import org.bmserver.core.common.domain.BaseDomainQuery
import org.bmserver.core.common.domain.Pagination
import org.springframework.data.relational.core.query.Criteria
import java.util.UUID

class AnswerRefQuery(
    val chat:UUID,
    override val pagination: Pagination?
):BaseDomainQuery(pagination) {
    override fun buildQuery(): Criteria {
        val criteriaList = mutableListOf<Criteria>()

        criteriaList.add(Criteria.where("chat").`is`(chat))

        return if (criteriaList.isNotEmpty()) {
            Criteria.from(criteriaList)
        } else {
            Criteria.empty()
        }
    }
}