package org.bmserver.docuhelperapi.domain.document

import org.bmserver.docuhelperapi.common.core.common.BaseDomainService
import org.bmserver.docuhelperapi.common.core.document.DocumentOutPort
import org.bmserver.docuhelperapi.common.core.document.model.Document
import org.springframework.stereotype.Service

@Service
class DocumentAdapter(
    private val baseDomainRepository: DocumentRepository,
    private val baseDomainQueryRepository: DocumentQueryRepository,
) : BaseDomainService<Document>(baseDomainRepository, baseDomainQueryRepository),
    DocumentOutPort
