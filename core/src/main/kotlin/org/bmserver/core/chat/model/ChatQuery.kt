package org.bmserver.core.chat.model

import org.bmserver.core.common.domain.BaseDomainQuery
import org.springframework.data.relational.core.query.Criteria
import java.util.UUID

class ChatQuery(
    private val user: UUID?,
    private val document: UUID?,
) : BaseDomainQuery() {

    override fun buildQuery(): Criteria {
        val criteriaList = mutableListOf<Criteria>()

        user?.let { criteriaList.add(Criteria.where("user_uuid").`is`(it)) }
        document?.let { criteriaList.add(Criteria.where("document").`is`(it)) }

        return if (criteriaList.isNotEmpty()) {
            Criteria.from(criteriaList)
        } else {
            Criteria.empty()
        }
    }
}