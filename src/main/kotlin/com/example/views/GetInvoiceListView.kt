package com.example.views

import com.example.models.Invoice
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun FlowContent.invoiceList(invoice: Invoice) {
    h2 { +"登録が無事に完了しました。" }
    p { +"会社名: ${invoice.company}" }
    p { +"管理者: ${invoice.personInCharge}" }
    p { +"ローン金額: ${invoice.loanAmount}" }
    p { +"開始日時: ${invoice.dateFrom.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    p { +"終了日時: ${invoice.dateTo.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    p { +"支払期日: ${invoice.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    a("/") { +"ホームに戻る" }
}
