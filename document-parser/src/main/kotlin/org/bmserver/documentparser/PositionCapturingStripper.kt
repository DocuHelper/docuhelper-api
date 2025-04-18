package org.bmserver.documentparser

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import java.io.Writer

// A Writer that discards all output
object NullWriter : Writer() {
    override fun write(cbuf: CharArray, off: Int, len: Int) { /* no-op */ }
    override fun flush() { /* no-op */ }
    override fun close() { /* no-op */ }
}

// Captures TextPosition objects for a given page
class PositionCapturingStripper(private val document: PDDocument) : PDFTextStripper() {
    val positions = mutableListOf<TextPosition>()
    init { sortByPosition = true }
    override fun writeString(text: String, textPositions: MutableList<TextPosition>) {
        positions += textPositions
        super.writeString(text, textPositions)
    }
    /**
     * Parses the specified page number (1-based) and returns all TextPosition entries.
     */
    fun capture(pageNum: Int): List<TextPosition> {
        startPage = pageNum
        endPage = pageNum
        positions.clear()
        // This will call writeString internally
        writeText(document, NullWriter)
        return positions.toList()
    }
}
