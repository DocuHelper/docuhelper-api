package org.bmserver.core.document.model

import org.bmserver.core.common.domain.BaseDomainQuery
import org.bmserver.core.common.domain.Pagination
import org.springframework.data.relational.core.query.Criteria
import java.util.UUID

data class DocumentQuery(
    private val uuid: UUID?,
    private val name: String?,
    private val state: DocumentState?,
    private val owner: UUID?,
    override val pagination: Pagination?
) : BaseDomainQuery(pagination) {
    override fun buildQuery(): Criteria {
        val criteriaList = mutableListOf<Criteria>()

        uuid?.let { criteriaList.add(Criteria.where("uuid").`is`(it)) }
        name?.let { criteriaList.add(Criteria.where("name").`is`(it)) }
        owner?.let { criteriaList.add(Criteria.where("owner").`is`(it)) }
        state?.let { criteriaList.add(Criteria.where("state").`is`(it)) }

        return if (criteriaList.isNotEmpty()) {
            Criteria.from(criteriaList)
        } else {
            Criteria.empty()
        }
    }
}
