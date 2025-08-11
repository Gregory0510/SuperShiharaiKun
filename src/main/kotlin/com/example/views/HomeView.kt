package com.example.views

import kotlinx.html.*

fun HTML.homePage() {
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