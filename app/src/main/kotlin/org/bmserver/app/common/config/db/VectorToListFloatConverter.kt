package org.bmserver.app.common.config.db

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class VectorToListFloatConverter : Converter<String, List<Float>> {
    override fun convert(source: String): List<Float> {

        return source
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .mapNotNull { it.trim().toFloatOrNull() }
    }
}
