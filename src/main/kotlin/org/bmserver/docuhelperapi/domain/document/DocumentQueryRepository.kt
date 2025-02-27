package org.bmserver.docuhelperapi.domain.document

import org.bmserver.docuhelperapi.common.core.common.BaseDomainQueryRepository
import org.bmserver.docuhelperapi.common.core.document.model.Document
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class DocumentQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
) : BaseDomainQueryRepository<Document>(r2dbcEntityTemplate, Document::class.java)
