package com.example.views

import kotlinx.html.*

fun HTML.profileForm() {
    head {
        title { +"User Profile Form" }
    }
    body {
        h1 { +"ユーザー情報を入力してください。" }
        form(action = "/profile", method = FormMethod.post) {
            p {
                label { +"企業名: " }
                textInput(name = "companyName")
            }
            p {
                label { +"氏名: " }
                textInput(name = "name")
            }
            p {
                label { +"メールアドレス: " }
                emailInput(name = "email")
            }
            p {
                label { +"パスワード: " }
                passwordInput(name = "password") {
                }
            }
            p {
                label { +"パスワード再確認: " }
                passwordInput(name = "passwordConfirm") {
                }
            }
            p {
                submitInput { value = "登録" }
            }
        }
    }
}
