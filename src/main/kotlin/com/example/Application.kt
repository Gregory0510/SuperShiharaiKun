package com.example

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import com.example.models.UserSession
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSessions()
    configureRouting()
    initDatabase()
}

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
        }
    }
}

fun initDatabase() {
    Database.connect(
        url = "jdbc:mysql://localhost:3306/shiharaikun?useSSL=false&serverTimezone=Asia/Tokyo",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root"
    )
}