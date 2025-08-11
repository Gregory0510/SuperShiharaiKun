package com.example.utils

import com.example.dto.UserSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

/**
 * セッション管理の設定
 */
suspend fun ApplicationCall.requireUserSession(): UserSession? {
    val session = sessions.get<UserSession>()
    if (session == null) {
        respondRedirect("/login")
    }
    return session
}