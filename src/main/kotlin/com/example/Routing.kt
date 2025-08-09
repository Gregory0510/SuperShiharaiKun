package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
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
            val company = params["company"] ?: "Unknown"
            val personInCharge = params["personInCharge"] ?: "N/A"
            val loanAmount = params["loanAmount"]!!.toBigDecimal()
            val dateFrom = params["dateFrom"].let {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            }
            val dateTo = params["dateTo"].let {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            }
            val dueDate = params["dueDate"].let {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            }

            val connection = connectToDatabase()
            val invoice = insertInvoice(connection, company, personInCharge, loanAmount, dateFrom, dateTo, dueDate, session.userId)
            connection.close()

            if (invoice == null) {
                call.respondText("Failed to save user profile.", status = io.ktor.http.HttpStatusCode.InternalServerError)
                return@post
            }

            println("Saved invoice: $invoice")

            call.respondHtml {
                head {
                    title { +"Profile Received" }
                }
                body {
                    invoiceList(invoice)
                }
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
            val dateFrom = params["dateFrom"].let {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            }
            val dateTo = params["dateTo"].let {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            }

            val invoiceList = fetchActiveDueDateInRange(connectToDatabase(), dateFrom, dateTo, session.userId)
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
            val params = call.receiveParameters()
            val name = params["name"] ?: "Unknown"
            val email = params["email"] ?: "N/A"
            val age = params["age"]?.toIntOrNull() ?: -1
            val password = params["password"] ?: ""

            val connection = connectToDatabase()
            val userProfile = insertUserProfile(connection, name, email, age, password, LocalDateTime.now(Clock.system(ZoneId.of("Asia/Tokyo"))))
            connection.close()

            if (userProfile == null) {
                call.respondText("Failed to save user profile.", status = io.ktor.http.HttpStatusCode.InternalServerError)
                return@post
            }

            println("Saved user profile: $userProfile")

            call.respondHtml {
                head {
                    title { +"Profile Received" }
                }
                body {
                    userProfileList(userProfile)
                }
            }
        }

        get("/users") {
            call.requireUserSession() ?: return@get

            val users = fetchAllUsers(connectToDatabase())
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

            val connection = connectToDatabase()

            // Fetch user from DB
            val stmt = connection.prepareStatement("SELECT user_id, password FROM UserProfile WHERE email = ?")
            stmt.setString(1, email)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val userId = rs.getInt("user_id")
                val hashedPassword = rs.getString("password")

                if (BCrypt.checkpw(password, hashedPassword)) {
                    call.sessions.set(UserSession(userId, email))
                    call.respondRedirect("/") // or dashboard
                } else {
                    call.respondText("Invalid email or password")
                }
            } else {
                call.respondText("Invalid email or password")
            }

            rs.close()
            stmt.close()
            connection.close()
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }
}
