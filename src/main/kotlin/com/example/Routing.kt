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
import com.example.models.UserSession
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
            call.requireUserSession() ?: return@get

            call.respondHtml {
                homePage()
            }
        }

        // WEB用の請求書登録画面-get
        get("/invoice") {
            call.requireUserSession() ?: return@get

            call.respondHtml {
                invoiceForm()
            }
        }

        // WEB用の請求書登録-post
        post("/web/invoice") {
            val session = call.requireUserSession() ?: return@post
            val params = call.receiveParameters()

            try {
                val invoicesInput = InvoicesInput.fromParameters(params, session.userId)
                val invoice = insertInvoice(invoicesInput)

                call.respondHtml {
                    invoiceList(invoice)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("Failed to save invoices: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用の請求書検索画面-get
        get("/invoice/list") {
            call.requireUserSession() ?: return@get

            call.respondHtml {
                invoiceSearchForm()
            }
        }

        // WEB用の請求書検索-post
        post("/invoice/list") {
            val session = call.requireUserSession() ?: return@post
            val params = call.receiveParameters()

            val dateFrom = LocalDate.parse(params["dateFrom"]!!)
            val dateTo = LocalDate.parse(params["dateTo"]!!)

            val invoiceList = fetchActiveDueDateInRange(dateFrom, dateTo, session.userId)
            call.respondHtml {
                invoiceTable(invoiceList)
            }
        }

        // WEB用のユーザー登録画面-get
        get("/profile") {
            call.respondHtml {
                profileForm()
            }
        }

        // WEB用のユーザ登録-post
        post("/profile") {
            val params = call.receiveParameters()

            try {
                val usersInput = UsersInput.fromParameters(params)
                val users = insertUserProfile(usersInput)

                call.respondHtml {
                    userProfileList(users)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("Failed to save Users: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // WEB用のユーザー一覧-get
        get("/users") {
            call.requireUserSession() ?: return@get

            val users = fetchAllUsers()
            call.respondHtml {
                userTable(users)
            }
        }

        // WEB用のログイン画面-get
        get("/login") {
            call.respondHtml {
                loginForm()
            }
        }

        // WEB用のログイン-post
        post("/web/login") {
            val params = call.receiveParameters()
            val email = params["email"] ?: ""
            val password = params["password"] ?: ""

            // DBから取得
            val user = fetchUserByEmail(email)

            if (user !== null) {
                val userId = user.userId
                val hashedPassword = user.password

                if (BCrypt.checkpw(password, hashedPassword)) {
                    call.sessions.set(UserSession(userId, email))
                    call.respondRedirect("/")
                } else {
                    call.respondText("メールアドレス、またはパスワードが間違えています。")
                }
            } else {
                call.respondText("メールアドレス、またはパスワードが間違えています。")
            }
        }

        // API用のログイン-post
        post("/api/login") {
            val params = call.receiveParameters()
            val email = params["email"] ?: return@post call.respondText("Missing email")
            val password = params["password"] ?: return@post call.respondText("Missing password")

            val user = fetchUserByEmail(email)
            if (user == null || !BCrypt.checkpw(password, user.password)) {
                return@post call.respondText("メールアドレス、またはパスワードが間違えています。", status = HttpStatusCode.Unauthorized)
            }

            val token = generateToken(user.userId)
            call.respond(mapOf("token" to token))
        }

        // JWT認証が必要なAPIはこちら
        authenticate("auth-jwt") {
            // API用の請求書登録-post
            post("/api/invoice") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()
                val params = call.receiveParameters()

                try {
                    val invoicesInput = InvoicesInput.fromParameters(params, userId)
                    val invoice = insertInvoice(invoicesInput)

                    // HTMLの代わりにJSONを返却
                    call.respond(invoice.toDTO())
                } catch (e: Exception) {
                    e.printStackTrace() // Consider using logging
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
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()

                val params = call.receiveParameters()
                val dateFrom = LocalDate.parse(params["dateFrom"]!!)
                val dateTo = LocalDate.parse(params["dateTo"]!!)

                val invoiceList = fetchActiveDueDateInRange(dateFrom, dateTo, userId)
                val outputList = invoiceList.map { it.toDTO() }
                call.respond(outputList)
            }
        }

        // ログアウト
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }
}