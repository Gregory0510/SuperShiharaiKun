package com.example.views

import com.example.models.Users
import kotlinx.html.*
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.format.DateTimeFormatter

fun FlowContent.userTable(users: List<Users>) {
    h1 { +"ユーザー一覧" }
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
                td { +user.createdAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
                td { +user.updatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
            }
            println("Raw: ${user.createdAt}")
            println("Formatted: ${user.createdAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}")

        }
    }
    br
    a("/") { +"ホームに戻る" }
}