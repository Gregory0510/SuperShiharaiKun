package com.example.views

import kotlinx.html.*

fun HTML.loginForm() {
    head {
        title { +"Login" }
    }
    body {
        form(action = "/web/login", method = FormMethod.post) {
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