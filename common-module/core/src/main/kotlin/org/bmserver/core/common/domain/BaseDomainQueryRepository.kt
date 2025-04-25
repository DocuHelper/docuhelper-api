package org.bmserver.core.common.domain

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import reactor.core.publisher.Flux

abstract class BaseDomainQueryRepository<T : BaseDomain>(
    private val template: R2dbcEntityTemplate,
    private val classType: Class<T>,
) {
    fun searchQuery(query: BaseDomainQuery): Flux<T> {
        val whereBuilder = query.buildQuery()
        val pagination = query.pagination
        return fetchAll(whereBuilder, pagination)
    }

    private fun fetchAll(criteria: Criteria, pagination: Pagination?): Flux<T> {
        val sql = template.select(classType)

//        criteria.and(Cert) TODO 공통 추가

        val query = if (pagination == null) {
            Query.query(criteria)
        } else {
            Query.query(criteria).limit(pagination.limit).offset(pagination.offset)
        }

        return sql
            .matching(query)
            .all()
    }
}
