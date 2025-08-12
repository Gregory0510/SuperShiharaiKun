package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * JWTトクーンを生成する
 */
fun generateToken(userId: Int): String {
    /**
     * TODO:
     * セキュアなキー情報をもっと安全な場所に保存する必要があることは理解していますが、これはデモ用なので、
     * すべてを同じパッケージ内にまとめて、少ない設定でどのコンピュータにも簡単にデプロイできるようにしています。
     * 将来的には、キーは環境変数、AWS Secrets Manager、HSMなどに保存することを推奨します。
     */
    val secret = "h6GFiT76j(54H!hsSu8JDs?fH"
    val issuer = "ktor.io"
    val audience = "ktor_audience"
    val expiresAt = Date(System.currentTimeMillis() + 36_000_00) // 1時間

    return JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("userId", userId)
        .withExpiresAt(expiresAt)
        .sign(Algorithm.HMAC256(secret))
}