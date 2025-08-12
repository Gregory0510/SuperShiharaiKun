package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import com.example.repository.*
import com.example.views.*
import io.ktor.server.sessions.*
import org.mindrot.jbcrypt.BCrypt
import com.example.dto.UserSession
import com.example.utils.requireUserSession
import com.example.dto.InvoicesInput
import com.example.dto.UsersInput
import io.ktor.http.HttpStatusCode
import java.time.LocalDate
import com.example.utils.generateToken
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import com.example.dao.*

fun Application.configureRouting() {
    routing {
        // WEB用のホーム画面-get
        get("/") {
            val session = call.requireUserSession() ?: return@get
            try {
                call.respondHtml {
                    homePage()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ホームページの表示に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用の請求書登録画面-get
        get("/invoice") {
            val session = call.requireUserSession() ?: return@get
            try {
                call.respondHtml {
                    invoiceForm()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("請求書登録画面の表示に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用の請求書登録-post
        post("/web/invoice") {
            val session = call.requireUserSession() ?: return@post
            try {
                val params = call.receiveParameters()
                val invoicesInput = InvoicesInput.fromParameters(params, session.userId)
                val invoice = insertInvoice(invoicesInput)
                call.respondHtml {
                    invoiceList(invoice)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("請求書の登録に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用の請求書検索画面-get
        get("/invoice/list") {
            val session = call.requireUserSession() ?: return@get
            try {
                call.respondHtml {
                    invoiceSearchForm()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("請求書検索画面の表示に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用の請求書検索-post
        post("/invoice/list") {
            val session = call.requireUserSession() ?: return@post
            try {
                val params = call.receiveParameters()
                val dateFromStr = params["dateFrom"]
                val dateToStr = params["dateTo"]

                if (dateFromStr.isNullOrBlank() || dateToStr.isNullOrBlank()) {
                    return@post call.respondText("開始日時または終了日時が入力されていません。", status = HttpStatusCode.BadRequest)
                }

                val dateFrom = try {
                    LocalDate.parse(dateFromStr)
                } catch (e: Exception) {
                    return@post call.respondText("開始日時の形式が不正です。", status = HttpStatusCode.BadRequest)
                }

                val dateTo = try {
                    LocalDate.parse(dateToStr)
                } catch (e: Exception) {
                    return@post call.respondText("終了日時の形式が不正です。", status = HttpStatusCode.BadRequest)
                }

                val invoiceList = fetchActiveDueDateInRange(dateFrom, dateTo, session.userId)
                call.respondHtml {
                    invoiceTable(invoiceList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("請求書一覧の検索に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のユーザー登録画面-get
        get("/profile") {
            try {
                call.respondHtml {
                    profileForm()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ユーザー登録画面の表示に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のユーザ登録-post
        post("/profile") {
            try {
                val params = call.receiveParameters()
                if (params["password"] != params["passwordConfirm"]) {
                    return@post call.respondText("パスワードが一致していません。", status = HttpStatusCode.BadRequest)
                }
                val usersInput = UsersInput.fromParameters(params)
                val users = insertUserProfile(usersInput)

                call.respondHtml {
                    userProfileList(users)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ユーザーの登録に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のユーザー一覧-get
        get("/users") {
            val session = call.requireUserSession() ?: return@get
            try {
                val users = fetchAllUsers()
                call.respondHtml {
                    userTable(users.toDTOList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ユーザーの取得に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のログイン画面-get
        get("/login") {
            try {
                call.respondHtml {
                    loginForm()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ログイン画面の表示に失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のログイン-post
        post("/web/login") {
            try {
                val params = call.receiveParameters()
                val email = params["email"] ?: return@post call.respondText("Email missing", status = HttpStatusCode.BadRequest)
                val password = params["password"] ?: return@post call.respondText("Password missing", status = HttpStatusCode.BadRequest)

                val user = fetchUserByEmail(email)
                if (user != null && BCrypt.checkpw(password, user.password)) {
                    call.sessions.set(UserSession(user.userId, email))
                    call.respondRedirect("/")
                } else {
                    call.respondText("メールアドレス、またはパスワードが間違えています。", status = HttpStatusCode.Unauthorized)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ログインに失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // API用のログイン-post
        post("/api/login") {
            try {
                val params = call.receiveParameters()
                val email = params["email"] ?: return@post call.respondText("Missing email", status = HttpStatusCode.BadRequest)
                val password = params["password"] ?: return@post call.respondText("Missing password", status = HttpStatusCode.BadRequest)

                val user = fetchUserByEmail(email)
                if (user == null || !BCrypt.checkpw(password, user.password)) {
                    return@post call.respondText("メールアドレス、またはパスワードが間違えています。", status = HttpStatusCode.Unauthorized)
                }

                val token = generateToken(user.userId)
                call.respond(mapOf("token" to token))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ログインに失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // JWT認証が必要なAPIはこちら
        authenticate("auth-jwt") {
            // API用の請求書登録-post
            post("/api/invoice") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    if (principal == null) {
                        return@post call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                    }
                    val userId = principal.payload.getClaim("userId").asInt()
                    val params = call.receiveParameters()

                    val invoicesInput = InvoicesInput.fromParameters(params, userId)
                    val invoice = insertInvoice(invoicesInput)

                    call.respond(invoice.toDTO())
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf(
                            "status" to "error",
                            "message" to (e.message ?: "Unknown error")
                        )
                    )
                }
            }

            // API用の請求書検索-post
            post("/api/invoice/list") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    if (principal == null) {
                        return@post call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                    }
                    val userId = principal.payload.getClaim("userId").asInt()

                    val params = call.receiveParameters()
                    val dateFromStr = params["dateFrom"]
                    val dateToStr = params["dateTo"]

                    if (dateFromStr.isNullOrBlank() || dateToStr.isNullOrBlank()) {
                        return@post call.respondText("Missing dateFrom or dateTo parameters", status = HttpStatusCode.BadRequest)
                    }

                    val dateFrom = try {
                        LocalDate.parse(dateFromStr)
                    } catch (e: Exception) {
                        return@post call.respondText("Invalid dateFrom format", status = HttpStatusCode.BadRequest)
                    }

                    val dateTo = try {
                        LocalDate.parse(dateToStr)
                    } catch (e: Exception) {
                        return@post call.respondText("Invalid dateTo format", status = HttpStatusCode.BadRequest)
                    }

                    val invoiceList = fetchActiveDueDateInRange(dateFrom, dateTo, userId)
                    val outputList = invoiceList.map { it.toDTO() }
                    call.respond(outputList)
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf(
                            "status" to "error",
                            "message" to (e.message ?: "Unknown error")
                        )
                    )
                }
            }
        }

        // ログアウト
        get("/logout") {
            try {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/login")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("ログアウトに失敗しました。: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
