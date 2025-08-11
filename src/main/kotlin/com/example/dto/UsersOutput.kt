package com.example.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UsersOutput(
    val userId: Int,
    val companyName: String,
    val name: String,
    val email: String,
    val password: String,
    @Contextual val createdAt: String,
    @Contextual val updatedAt: String
)

