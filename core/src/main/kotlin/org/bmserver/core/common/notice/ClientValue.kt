package org.bmserver.core.common.notice

import java.util.UUID

/**
 * 유저 클라이언트 접속 정보
 */
class ClientValue (
    /**
     * key 서버 UUID
     * value 클라이언트 수
     */
    val clients: MutableMap<UUID,Int> = mutableMapOf()
)
