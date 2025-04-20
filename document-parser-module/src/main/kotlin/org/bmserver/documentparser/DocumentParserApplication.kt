package org.bmserver.documentparser

import org.springframework.ai.reader.ExtractedTextFormatter
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class DocumentParserApplication

//	runApplication<DocumentParserApplication>(*args)

fun main() {
    val pdfUrl = listOf(
//        "test/pdf/DeepSeek_R1.pdf",
//        "test/pdf/HC-SR04Users_Manual.pdf",
//        "test/pdf/노동위원회법(법률)(제18179호)(20220519) 복사본.pdf",
        "test/pdf/자료집_20240226_좌담회_송파세모녀10주기.pdf",
//        "test/pdf/통계자료를 활용한 농산물의 탄소배출량 산정 복사본.pdf",
//        "test/pdf/12. 사회복지실천론_중간.pdf",
//        "test/pdf/레이저를 이용한 TSV 드릴링 공정 복사본.pdf",
//        "test/pdf/SiC 전력반도체 소자기술 동향.pdf"
//        "test/pdf/정민상 이력서 복사본.pdf"
    )
    val config =
        PdfDocumentReaderConfig.builder()
            .withPageTopMargin(0) // 페이지 상단 margin 제거
            .withPageExtractedTextFormatter(
                ExtractedTextFormatter.builder()
                    .withNumberOfTopTextLinesToDelete(0) // 텍스트 윗부분 제거 안 함
                    .build()
            )
            .withPagesPerDocument(1) // 페이지당 Document 하나 생성
            .build()

    pdfUrl.forEach {
        println("================================================================")
        println("try it : ${it}")
        val reader =
            CustomPagePdfDocumentReader(it,config)
//            PagePdfDocumentReader(it, config)

        val documents = reader.read() // 문단 단위로 Document 리스트 반환

        documents.forEach {
            println(it.text)
        }

    }

}