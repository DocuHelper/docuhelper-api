package org.bmserver.core.common

abstract class CommonGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
)
