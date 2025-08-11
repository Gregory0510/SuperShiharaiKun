package com.example.views

import com.example.models.Users
import kotlinx.html.*
import com.example.utils.formatDateTimeToString

fun HTML.userTable(users: List<Users>) {
    head { title { +"User Profiles" } }
    body { h1 { +"ユーザー一覧" }
        table {
            attributes["border"] = "1"
            tr {
                th { +"企業名" }
                th { +"氏名" }
                th { +"メールアドレス" }
                th { +"作成日時" }
                th { +"更新日時" }
            }
            for (user in users) {
                tr {
                    td { +user.companyName }
                    td { +user.name }
                    td { +user.email }
                    td { +formatDateTimeToString(user.createdAt) }
                    td { +formatDateTimeToString(user.updatedAt) }
                }
            }
        }
        br
        a("/") { +"ホームに戻る" }
    }
}