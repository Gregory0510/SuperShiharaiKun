package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Users(
    val userId: Int,
    val companyName: String,
    val name: String,
    val email: String,
    val password: String,
    @Contextual val createdAt: LocalDateTime,
    @Contextual val updatedAt: LocalDateTime
)