package com.example

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import com.example.models.UserSession

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSessions()
    configureRouting()
}

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
        }
    }
}