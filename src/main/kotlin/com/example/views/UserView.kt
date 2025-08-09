package com.example.views

import com.example.models.UserProfile
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun FlowContent.userTable(users: List<UserProfile>) {
    h1 { +"ユーザー一覧" }
    table {
        attributes["border"] = "1"
        tr {
            th { +"ID" }
            th { +"姓名" }
            th { +"メールアドレス" }
            th { +"年齢" }
            th { +"最終更新日時" }
        }
        for (user in users) {
            tr {
                td { +user.userId.toString() }
                td { +user.name }
                td { +user.email }
                td { +user.age.toString() }
                td { +user.lastUpdateDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
            }
            println("Raw: ${user.lastUpdateDate}")
            println("Formatted: ${user.lastUpdateDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}")

        }
    }
    br
    a("/") { +"ホームに戻る" }
}