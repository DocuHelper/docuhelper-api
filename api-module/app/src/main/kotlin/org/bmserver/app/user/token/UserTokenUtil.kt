package org.bmserver.app.user.token

import org.bmserver.core.user.model.UserRole


object UserRoleTokenPolicy {
    fun tokenLimitOf(role: UserRole): Int {
        return when (role) {
            UserRole.FREE -> 10000
            UserRole.BASIC -> 20000
            UserRole.PREMIUM -> 30000
            UserRole.ENTERPRISE -> 40000
        }
    }
}