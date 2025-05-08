package org.bmserver.app.test

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TestQARepository: R2dbcRepository<TestQA, UUID> {
}