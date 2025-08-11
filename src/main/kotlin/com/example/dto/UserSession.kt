package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val userId: Int, val email: String)