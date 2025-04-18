package org.bmserver.documentparser

import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.text.TextPosition
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.document.DocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.reader.pdf.layout.PDFLayoutTextStripperByArea
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.util.StringUtils
import java.io.IOException

/**
 * 2열(Left/Right) 구조의 PDF 페이지를 읽어들여
 * 좌우 컬럼 순서대로 텍스트를 추출하는 커스텀 리더입니다.
 */
class CustomPagePdfDocumentReader : DocumentReader {

    companion object {
        const val METADATA_START_PAGE_NUMBER = "page_number"
        const val METADATA_END_PAGE_NUMBER = "end_page_number"
        const val METADATA_FILE_NAME = "file_name"
        private const val DEFAULT_ESTIMATED_LINE_HEIGHT = 14f
        private const val DEFAULT_MIN_CHARS_PER_COLUMN = 5
    }

    private val document: PDDocument
    private val resourceFileName: String?
    private val config: PdfDocumentReaderConfig
    private val logger = LoggerFactory.getLogger(CustomPagePdfDocumentReader::class.java)

    constructor(resourceUrl: String) : this(DefaultResourceLoader().getResource(resourceUrl))
    constructor(pdfResource: Resource) : this(pdfResource, PdfDocumentReaderConfig.defaultConfig())
    constructor(resourceUrl: String, config: PdfDocumentReaderConfig)
            : this(DefaultResourceLoader().getResource(resourceUrl), config)

    constructor(pdfResource: Resource, config: PdfDocumentReaderConfig) {
        try {
            val parser = PDFParser(org.apache.pdfbox.io.RandomAccessReadBuffer(pdfResource.inputStream))

            this.document = parser.parse()
            this.resourceFileName = pdfResource.filename
            this.config = config
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun get(): List<Document> {
        val readDocuments = mutableListOf<Document>()
        try {
            val pdfTextStripper = PDFLayoutTextStripperByArea()
            var pageNumber = 0
            var pagesPerDocument = 0
            var startPageNumber = 0
            val pageTextGroupList = mutableListOf<String>()

            val pages = document.documentCatalog.pages
            val totalPages = pages.count
            val logFrequency = if (totalPages > 10) totalPages / 10 else 1
            var counter = 0
            var lastPage: PDPage? = null

            for (page in pages) {
                lastPage = page
                if (counter % logFrequency == 0 && counter / logFrequency < 10) {
                    logger.info("Processing PDF page: ${counter + 1}")
                }
                counter++
                pagesPerDocument++

                // 페이징 단위 맞춤
                if (config.pagesPerDocument != PdfDocumentReaderConfig.ALL_PAGES
                    && pagesPerDocument >= config.pagesPerDocument
                ) {
                    pagesPerDocument = 0
                    val aggregated = pageTextGroupList.joinToString("")
                    if (StringUtils.hasText(aggregated)) {
                        readDocuments.add(toDocument(page, aggregated, startPageNumber, pageNumber))
                    }
                    pageTextGroupList.clear()
                    startPageNumber = pageNumber + 1
                }

                // 동적 레이아웃 감지: 글자 위치 획득
                val positionStripper = PositionCapturingStripper(document)
                val allPositions: List<TextPosition> = positionStripper.capture(pageNumber + 1)

                // 커스텀 S
                val groupYField = allPositions
                    .groupBy { it.y }
                    .mapKeys {
                        Pair(
                            Pair(it.value.first().x, it.value.first().y),
                            Pair(it.value.last().x, it.value.last().y+it.value.last().height)
                        )
                    }

                val addContent = mutableMapOf<Pair<Pair<Float, Float>, Pair<Float, Float>>, List<TextPosition>>()
                val content = mutableListOf<String>()
                var temp = 0
                groupYField.forEach {
                    val pageSize = Pair(it.value.first().pageWidth, it.value.first().pageHeight)
                    val pageCenterStandard = pageSize.first / 2
                    val section = it.value.map { it.toString() }.joinToString("") + "\n"

                    val sectionPosition = it.key

                    val startPositionX = sectionPosition.first.first
                    val endPositionX = sectionPosition.second.first

                    val startPositionY = sectionPosition.first.second
                    val endPositionY = sectionPosition.second.second


                    // 왼쪽
                    if (endPositionX < pageCenterStandard) {
                        content.add(temp, section)
                        temp += 1
                    } else if (startPositionX > pageCenterStandard) { // 오른쪽
                        val leftContent =
                            addContent.filter {
                                (startPositionY in it.key.first.second ..it.key.second.second)
                                        ||
                                        (endPositionY in it.key.first.second ..it.key.second.second)
                            }
                        //왼쪽에 없으면®
                        if (leftContent.isEmpty()) {
                            content.add(temp, section)
                            temp += 1
                        } else { //왼쪽에 있으면
                            content.add(temp, section)
                        }
                    } else { // 중앙
                        content.add(temp, section)
                        temp += 1
                    }

                    addContent.put(it.key, it.value)

                }

                // 커스텀 E

                pageTextGroupList += content
                pageNumber++
            }

            // 남은 텍스트 처리
            if (lastPage != null && pageTextGroupList.isNotEmpty()) {
                val aggregated = pageTextGroupList.joinToString("")
                readDocuments.add(toDocument(lastPage, aggregated, startPageNumber, pageNumber))
            }
            logger.info("Processed {} pages", totalPages)
            return readDocuments
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun toDocument(
        page: PDPage,
        docText: String,
        startPageNumber: Int,
        endPageNumber: Int
    ): Document {
        val doc = Document(docText)
        doc.metadata[METADATA_START_PAGE_NUMBER] = startPageNumber
        if (startPageNumber != endPageNumber) {
            doc.metadata[METADATA_END_PAGE_NUMBER] = endPageNumber
        }
        resourceFileName?.let { doc.metadata[METADATA_FILE_NAME] = it }
        return doc
    }
}

