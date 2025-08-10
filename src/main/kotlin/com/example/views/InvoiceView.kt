package com.example.views

import com.example.db.InvoicesDAO
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FlowContent.invoiceTable(invoiceList: List<InvoicesDAO>) {
    h1 { +"請求書の検索結果" }
    table {
        attributes["border"] = "1"
        tr {
            th { +"支払金額" }
            th { +"支払期日" }
            th { +"発行日" }
            th { +"手数料" }
            th { +"手数料率" }
            th { +"消費税" }
            th { +"消費税率" }
            th { +"請求金額" }
            th { +"作成日時" }
            th { +"更新日時" }
        }
        for (invoice in invoiceList) {
            tr {
                td { +invoice.paymentAmount.toString() }
                td { +invoice.paymentDueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) }
                td { +invoice.issueDate.atZone(ZoneId.of("Asia/Tokyo")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
                td { +invoice.fee.toString() }
                td { +invoice.feeRate.toString() }
                td { +invoice.taxAmount.toString() }
                td { +invoice.taxRate.toString() }
                td { +invoice.totalAmount.toString() }
                td { +invoice.createdAt.atZone(ZoneId.of("Asia/Tokyo")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
                td { +invoice.updatedAt.atZone(ZoneId.of("Asia/Tokyo")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) }
            }
        }
    }
    br
    a("/") { +"ホームに戻る" }
}