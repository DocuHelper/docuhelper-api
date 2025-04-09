package org.bmserver.core.common

import java.util.UUID

class Config {
    companion object {
        val serverUuid: UUID = UUID.randomUUID()
//        val serverUuid: UUID = UUID.fromString("dd9a4830-3bac-4763-8634-0e5bebab2c9c")
    }
}