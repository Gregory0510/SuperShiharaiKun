package com.example.views

import com.example.models.Invoice
import com.example.models.UserProfile
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun FlowContent.invoiceTable(invoiceList: List<Invoice>) {
    h1 { +"請求書の検索結果" }
    table {
        attributes["border"] = "1"
        tr {
            th { +"会社名" }
            th { +"管理者" }
            th { +"ローン金額" }
            th { +"開始日時" }
            th { +"終了日時" }
            th { +"支払期日" }
        }
        for (invoice in invoiceList) {
            tr {
                td { +invoice.company }
                td { +invoice.personInCharge }
                td { +invoice.loanAmount.toString() }
                td { +invoice.dateFrom.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
                td { +invoice.dateTo.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
                td { +invoice.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
            }
        }
    }
    br
    a("/") { +"ホームに戻る" }
}