package org.bmserver.app.document

import org.bmserver.app.common.core.common.BaseDomainRepository
import org.bmserver.app.common.core.document.model.Document
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : BaseDomainRepository<Document>
