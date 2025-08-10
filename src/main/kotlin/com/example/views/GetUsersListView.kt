package com.example.views

import com.example.db.UsersDAO
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FlowContent.userProfileList(user: UsersDAO) {
    h2 { +"登録が無事に完了しました。" }
    p { +"企業名: ${user.companyName}" }
    p { +"氏名: ${user.name}" }
    p { +"メールアドレス: ${user.email}" }
    p { +"パスワード: ********" }

    // Convert Instant to LocalDateTime before formatting
    val createdAtLocalDateTime = user.createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val updatedAtLocalDateTime = user.updatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime()

    p { +"作成日時: ${createdAtLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    p { +"更新日時: ${updatedAtLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    a("/") { +"ホームに戻る" }
}