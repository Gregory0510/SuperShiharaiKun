package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateToken(userId: Int): String {
    val secret = "your-secret-key"
    val issuer = "ktor.io"
    val audience = "ktor_audience"
    val expiresAt = Date(System.currentTimeMillis() + 36_000_00) // 1 hour

    return JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("userId", userId)
        .withExpiresAt(expiresAt)
        .sign(Algorithm.HMAC256(secret))
}