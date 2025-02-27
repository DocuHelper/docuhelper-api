package org.bmserver.docuhelperapi.gateway.document

import org.bmserver.docuhelperapi.common.core.document.DocumentOutPort
import org.bmserver.docuhelperapi.common.core.document.model.Document
import org.bmserver.docuhelperapi.common.core.document.model.DocumentQuery
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/document")
class DocumentGateWay(
    private val documentOutPort: DocumentOutPort,
) {
    @GetMapping
    fun find(documentQuery: DocumentQuery) = documentOutPort.find(documentQuery)

    @PostMapping
    fun create(
        @RequestBody document: Document,
    ) = documentOutPort.create(document)

    @DeleteMapping
    fun delete(uuid: UUID) = documentOutPort.delete(uuid)
}
