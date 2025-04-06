package org.bmserver.app.common.config.db

import io.r2dbc.postgresql.codec.Vector
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class VectorToListFloatConverter : Converter<Vector, List<Float>> {
    override fun convert(source: Vector): List<Float> {

        return source
            .vector
            .toList()
    }
}
