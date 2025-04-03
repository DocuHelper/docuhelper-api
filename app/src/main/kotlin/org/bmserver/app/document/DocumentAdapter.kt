package org.bmserver.app.document

import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.common.domain.event.config.EventPublisher
import org.bmserver.core.common.notice.UserNotifier
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.event.DocumentCreate
import org.bmserver.core.document.model.Document
import org.bmserver.core.document.model.DocumentState
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class DocumentAdapter(
    private val baseDomainRepository: DocumentRepository,
    private val baseDomainQueryRepository: DocumentQueryRepository,
    private val eventPublisher: EventPublisher,
    private val userNotifier: UserNotifier
) : BaseDomainService<Document>(baseDomainRepository, baseDomainQueryRepository),
    DocumentOutPort {
    override fun create(model: Document): Mono<Document> {
        return super.create(model)
            .flatMap { eventPublisher.publish(DocumentCreate(it)).thenReturn(it) }
            .flatMap { userNotifier.send(it.owner, it).thenReturn(it) }

    }

    override fun updateDocumentState(uuid: UUID, state: DocumentState): Mono<Document> {
        return baseDomainRepository.findById(uuid)
            .map { it.updateDocumentState(state) }
            .flatMap { baseDomainRepository.save(it) }
            .flatMap { userNotifier.send(it.owner, it).thenReturn(it) }
    }
}
