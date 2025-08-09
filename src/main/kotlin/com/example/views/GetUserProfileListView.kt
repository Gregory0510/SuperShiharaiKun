package com.example.views

import com.example.models.UserProfile
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun FlowContent.userProfileList(user: UserProfile) {
    h2 { +"登録が無事に完了しました。" }
    p { +"姓名: ${user.name}" }
    p { +"メールアドレス: ${user.email}" }
    p { +"年齢: ${user.age}" }
    p { +"最終更新日時: ${user.lastUpdateDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    a("/") { +"ホームに戻る" }
}
