package com.example.views

import com.example.dao.InvoicesDAO
import kotlinx.html.*
import com.example.utils.formatDateTimeToString
import com.example.utils.formatDateWithSlash
import com.example.utils.formatYen

fun HTML.invoiceTable(invoiceList: List<InvoicesDAO>) {
    head { title { +"請求書" } }
    body { h1 { +"請求書の検索結果" }
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
                    td { +formatYen(invoice.paymentAmount) }
                    td { +formatDateWithSlash(invoice.paymentDueDate) }
                    td { +formatDateWithSlash(invoice.issueDate) }
                    td { +formatYen(invoice.fee) }
                    td { +invoice.feeRate.toString() }
                    td { +formatYen(invoice.taxAmount) }
                    td { +invoice.taxRate.toString() }
                    td { +formatYen(invoice.totalAmount) }
                    td { +formatDateTimeToString(invoice.createdAt) }
                    td { +formatDateTimeToString(invoice.updatedAt) }
                }
            }
        }
        br
        a("/") { +"ホームに戻る" }
    }
}