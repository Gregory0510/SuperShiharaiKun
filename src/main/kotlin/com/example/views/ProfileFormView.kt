package com.example.views

import kotlinx.html.*

fun FlowContent.profileForm() {
    h1 { +"ユーザー情報を入力してください。" }
    form(action = "/profile", method = FormMethod.post) {
        p {
            label { +"姓名: " }
            textInput(name = "name")
        }
        p {
            label { +"メールアドレス: " }
            emailInput(name = "email")
        }
        p {
            label { +"年齢: " }
            numberInput(name = "age") {
                min = "0"
                value = "20"
            }
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
