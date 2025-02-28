package org.bmserver.core.common

import org.springframework.data.relational.core.query.Criteria

abstract class BaseDomainQuery {
    abstract fun buildQuery(): Criteria
}
