package com.example.views

import com.example.dao.UsersDAO
import kotlinx.html.*
import com.example.utils.formatDateTimeToString

fun HTML.userProfileList(user: UsersDAO) {
    head {
        title { +"Users Received" }
    }
    body {
        h2 { +"登録が無事に完了しました。" }
        br
        p { +"企業名: ${user.companyName}" }
        p { +"氏名: ${user.name}" }
        p { +"メールアドレス: ${user.email}" }
        p { +"パスワード: ********" }
        p { +"作成日時: ${formatDateTimeToString(user.createdAt)}" }
        p { +"更新日時: ${formatDateTimeToString(user.updatedAt)}" }
        br
        a("/") { +"ホームに戻る" }
    }
}