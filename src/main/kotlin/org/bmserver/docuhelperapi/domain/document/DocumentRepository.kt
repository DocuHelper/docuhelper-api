package org.bmserver.docuhelperapi.domain.document

import org.bmserver.docuhelperapi.common.core.common.BaseDomainRepository
import org.bmserver.docuhelperapi.common.core.document.model.Document
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : BaseDomainRepository<Document>
