package org.bmserver.app.test

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TestContentElsRepository: ReactiveElasticsearchRepository<TestContentEls, UUID> {
}

@Repository
interface TestQaElsRepository: ReactiveElasticsearchRepository<TestQAEls, UUID> {
}