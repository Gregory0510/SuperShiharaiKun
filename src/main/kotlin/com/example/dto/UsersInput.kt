package com.example.dto

import java.time.LocalDateTime
import io.ktor.http.Parameters
import org.mindrot.jbcrypt.BCrypt
import java.time.ZoneId

data class UsersInput(
    val companyName: String,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromParameters(params: Parameters): UsersInput {
            val hashedPassword = BCrypt.hashpw(params["password"], BCrypt.gensalt())

            return UsersInput(
                companyName = params["companyName"] ?: "",
                name = params["name"] ?: "",
                email = params["email"] ?: "",
                password = hashedPassword,
                createdAt = LocalDateTime.now(ZoneId.of("Asia/Tokyo")),
                updatedAt = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
            )
        }
    }
}