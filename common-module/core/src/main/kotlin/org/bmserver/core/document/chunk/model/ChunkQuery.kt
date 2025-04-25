package org.bmserver.core.document.chunk.model

import org.bmserver.core.common.domain.BaseDomainQuery
import org.bmserver.core.common.domain.Pagination
import org.springframework.data.relational.core.query.Criteria
import java.util.UUID

class ChunkQuery(
    val uuids: List<UUID>?,
    override val pagination: Pagination?
) : BaseDomainQuery(pagination) {
    override fun buildQuery(): Criteria {
        val criteriaList = mutableListOf<Criteria>()

        uuids?.let { criteriaList.add(Criteria.where("uuid").`in`(it)) }

        return if (criteriaList.isNotEmpty()) {
            Criteria.from(criteriaList)
        } else {
            Criteria.empty()
        }
    }
}