package org.bmserver.core.common.domain

import org.springframework.data.relational.core.query.Criteria

abstract class BaseDomainQuery {
    abstract fun buildQuery(): Criteria
}
