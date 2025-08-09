package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserProfile(
    val userId: Int,
    val name: String,
    val email: String,
    val age: Int,
    val password: String,
    @Contextual val lastUpdateDate: LocalDateTime
)