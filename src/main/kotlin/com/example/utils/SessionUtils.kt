package com.example.utils

import com.example.models.UserSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.http.*

suspend fun ApplicationCall.requireUserSession(): UserSession? {
    val session = sessions.get<UserSession>()
    if (session == null) {
        respondRedirect("/login")
    }
    return session
}
