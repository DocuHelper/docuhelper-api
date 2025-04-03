package org.bmserver.core.common.domain

import org.springframework.data.relational.core.query.Criteria

abstract class BaseDomainQuery(
    open val pagination: Pagination?
) {
    abstract fun buildQuery(): Criteria
}
