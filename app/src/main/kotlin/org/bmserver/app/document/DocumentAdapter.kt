package org.bmserver.app.document

import org.bmserver.core.common.BaseDomainService
import org.bmserver.core.common.event.EventPublisher
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.event.DocumentCreate
import org.bmserver.core.document.model.Document
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DocumentAdapter(
    private val baseDomainRepository: DocumentRepository,
    private val baseDomainQueryRepository: DocumentQueryRepository,
    private val eventPublisher: EventPublisher,
) : BaseDomainService<Document>(baseDomainRepository, baseDomainQueryRepository),
    DocumentOutPort {
    override fun create(model: Document): Mono<Document> {
        return super.create(model)
            .flatMap {
                eventPublisher.publish(DocumentCreate(it)).thenReturn(it)
            }
    }
}
