package org.bmserver.core.common

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import reactor.core.publisher.Flux

abstract class BaseDomainQueryRepository<T : BaseDomain>(
    private val template: R2dbcEntityTemplate,
    private val classType: Class<T>,
) {
    fun searchQuery(criteria: BaseDomainQuery): Flux<T> {
        val whereBuilder = criteria.buildQuery()

        return fetchAll(whereBuilder)
    }

    private fun fetchAll(criteria: Criteria): Flux<T> {
        val sql = template.select(classType)

//        criteria.and(Cert) TODO 공통 추가

        val query = Query.query(criteria)

        return sql
            .matching(query)
            .all()
    }
}
