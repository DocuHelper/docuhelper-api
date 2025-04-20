package org.bmserver.core.common.domain

abstract class CommonGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
)
