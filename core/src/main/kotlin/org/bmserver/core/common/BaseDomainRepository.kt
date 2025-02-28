package org.bmserver.core.common

import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.UUID

interface BaseDomainRepository<T : BaseDomain> : R2dbcRepository<T, UUID>
