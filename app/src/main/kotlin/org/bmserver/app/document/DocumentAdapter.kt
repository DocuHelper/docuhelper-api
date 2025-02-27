package org.bmserver.app.document

import org.bmserver.app.common.core.common.BaseDomainService
import org.bmserver.app.common.core.document.DocumentOutPort
import org.bmserver.app.common.core.document.model.Document
import org.springframework.stereotype.Service

@Service
class DocumentAdapter(
    private val baseDomainRepository: DocumentRepository,
    private val baseDomainQueryRepository: DocumentQueryRepository,
) : BaseDomainService<Document>(baseDomainRepository, baseDomainQueryRepository),
    DocumentOutPort
