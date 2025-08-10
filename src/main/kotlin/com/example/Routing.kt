package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import kotlinx.html.*
import com.example.db.*
import com.example.views.*
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import io.ktor.server.sessions.*
import org.mindrot.jbcrypt.BCrypt
import com.example.models.UserSession
import com.example.utils.requireUserSession
import com.example.dto.InvoicesInput
import com.example.dto.UsersInput
import io.ktor.http.HttpStatusCode
import java.time.LocalDate

fun Application.configureRouting() {
    routing {
        get("/") {

            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondRedirect("/login")
                return@get
            }

            call.respondHtml {
                head {
                    style {
                        unsafe {
                            +"""
                html, body {
                    height: 100%;
                    margin: 0;
                }
                body {
                    display: flex;
                    flex-direction: column;
                    min-height: 100vh;
                }
                main {
                    flex-grow: 1;
                }
                footer {
                    background-color: #f2f2f2;
                    text-align: center;
                    padding: 1em;
                }
                """.trimIndent()
                        }
                    }
                    title { +"スーパー支払い君.com" }
                }
                body {
                    main {
                        h1 { +"Hello World スーパー支払い君.com!" }
                        a("/invoice") { +"①請求書登録" }
                        br
                        a("/invoice/list") { +"②請求書検索" }
                        br
                        a("/profile") { +"③ユーザ登録" }
                        br
                        br
                        a("/logout") { +"ログアウト" }
                    }
                    footer {
                        h3 { +"管理者の秘密メニュー" }
                        a("/users") { +"ユーザー一覧" }
                    }
                }
            }
        }

        // 請求書登録-get
        get("/invoice") {
            call.requireUserSession() ?: return@get

            call.respondHtml {
                head {
                    title { +"Invoice Form" }
                }
                body {
                    invoiceForm()
                }
            }
        }

        // 請求書登録-post
        post("/invoice") {
            val session = call.requireUserSession() ?: return@post
            val params = call.receiveParameters()

            try {
                val invoicesInput = InvoicesInput.fromParameters(params, session.userId)
                val invoice = insertInvoice(invoicesInput)

                println("Saved invoice: $invoice")

                call.respondHtml {
                    head {
                        title { +"User Profile Received" }
                    }
                    body {
                        invoiceList(invoice)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Or use logger
                call.respondText("Failed to save invoices: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        // 請求書検索-get
        get("/invoice/list") {
            call.requireUserSession() ?: return@get

            call.respondHtml {
                head {
                    title { +"Invoice Form" }
                }
                body {
                    invoiceSearchForm()
                }
            }
        }

        // 請求書検索-post
        post("/invoice/list") {
            val session = call.requireUserSession() ?: return@post

            val params = call.receiveParameters()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") // replace with your actual format

            val dateFrom = LocalDate.parse(params["dateFrom"]!!)
            val dateTo = LocalDate.parse(params["dateTo"]!!)

            val invoiceList = fetchActiveDueDateInRange(dateFrom, dateTo, session.userId)
            call.respondHtml {
                head { title { +"請求書" } }
                body { invoiceTable(invoiceList) }
            }
        }

        // ユーザー登録
        get("/profile") {
            call.respondHtml {
                head {
                    title { +"User Profile Form" }
                }
                body {
                    profileForm()
                }
            }
        }

        post("/profile") {

//            val session = call.requireUserSession() ?: return@post
            val params = call.receiveParameters()

            try {
                val usersInput = UsersInput.fromParameters(params)
                val users = insertUserProfile(usersInput)

                println("Saved user profile: $users")

                call.respondHtml {
                    head {
                        title { +"Users Received" }
                    }
                    body {
                        userProfileList(users)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Or use logger
                call.respondText("Failed to save Users: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }

        get("/users") {
            call.requireUserSession() ?: return@get

            val users = fetchAllUsers()
            call.respondHtml {
                head { title { +"User Profiles" } }
                body { userTable(users) }
            }
        }

        get("/login") {
            call.respondHtml {
                head {
                    title { +"Login" }
                }
                body {
                    form(action = "/login", method = FormMethod.post) {
                        p {
                            label { +"Email: " }
                            emailInput(name = "email")
                        }
                        p {
                            label { +"Password: " }
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput { value = "Login" }
                        }
                        br
                        br
                        a("/profile") { +"アカウントお持ちでない方こちらで無料登録" }
                    }
                }
            }
        }

        post("/login") {
            val params = call.receiveParameters()
            val email = params["email"] ?: ""
            val password = params["password"] ?: ""
            println("email: $email")
            // Fetch user from DB
            val user = fetchUserByEmail(email)
            println("user: $user")
            if (user !== null) {
                val userId = user.userId
                val hashedPassword = user.password

                if (BCrypt.checkpw(password, hashedPassword)) {
                    call.sessions.set(UserSession(userId, email))
                    call.respondRedirect("/") // or dashboard
                } else {
                    call.respondText("メールアドレス、またはパスワードが間違えています。")
                }
            } else {
                call.respondText("メールアドレス、またはパスワードが間違えています。")
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }
}
