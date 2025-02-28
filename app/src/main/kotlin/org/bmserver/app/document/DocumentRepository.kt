package org.bmserver.app.document

import org.bmserver.core.common.BaseDomainRepository
import org.bmserver.core.document.model.Document
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : BaseDomainRepository<Document>
