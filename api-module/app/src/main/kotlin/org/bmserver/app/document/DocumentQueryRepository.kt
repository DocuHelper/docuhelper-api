package org.bmserver.app.document

import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.document.model.Document
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class DocumentQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
) : BaseDomainQueryRepository<Document>(r2dbcEntityTemplate, Document::class.java)
