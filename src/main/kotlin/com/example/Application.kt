package com.example

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import com.example.models.UserSession
import org.jetbrains.exposed.sql.Database

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.serializers.BigDecimalSerializer
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSessions()
    configureSecurity()
    configureRouting()
    initDatabase()
    configureJson()
}

/**
 * ログインセッションを設定します。
 */
fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
        }
    }
}

/**
 * DBの接続情報を設定します。
 */
fun Application.initDatabase() {
    Database.connect(
        url = "jdbc:mysql://localhost:3306/shiharaikun?useSSL=false&serverTimezone=Asia/Tokyo",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root"
        /**
         * TODO:
         * セキュアなキー情報をもっと安全な場所に保存する必要があることは理解していますが、これはデモ用なので、
         * すべてを同じパッケージ内にまとめて、少ない設定でどのコンピュータにも簡単にデプロイできるようにしています。
         * 将来的には、キーは環境変数、AWS Secrets Manager、HSMなどに保存することを推奨します。
         */
    )
}

/**
 * JWT認証を設定します。
 */
fun Application.configureSecurity() {
    val jwtIssuer = "ktor.io"
    val jwtAudience = "ktor_audience"
    val jwtRealm = "ktor_sample"
    val jwtSecret = "h6GFiT76j(54H!hsSu8JDs?fH"
    /**
     * TODO:
     * セキュアなキー情報をもっと安全な場所に保存する必要があることは理解していますが、これはデモ用なので、
     * すべてを同じパッケージ内にまとめて、少ない設定でどのコンピュータにも簡単にデプロイできるようにしています。
     * 将来的には、キーは環境変数、AWS Secrets Manager、HSMなどに保存することを推奨します。
     */

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .withAudience(jwtAudience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

/**
 * APIでJSONレスポンスを返すために使用するファイルを設定します。
 */
fun Application.configureJson() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            allowStructuredMapKeys = true

            serializersModule = SerializersModule {
                contextual(BigDecimal::class, BigDecimalSerializer)
            }
        })
    }
}
