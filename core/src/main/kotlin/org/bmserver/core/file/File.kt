package org.bmserver.core.file

import org.springframework.data.annotation.Id
import java.util.UUID

data class File(
    @Id val uuid: UUID?,
    val name: String,
    val extension: String,
    val size: Long,
    var isUsed: Boolean = false,
//    val createAt: DateTime? = DateTime.now(),  TODO
//    val updateAt: DateTime? = DateTime.now(),  TODO
) {
    fun isUsed() {
        isUsed = true
    }
}
