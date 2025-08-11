package com.example.views

import com.example.dao.InvoicesDAO
import kotlinx.html.*
import com.example.utils.formatDateTimeToString
import com.example.utils.formatDateWithSlash
import com.example.utils.formatYen

fun HTML.invoiceList(invoice: InvoicesDAO) {
    head {
        title { +"Invoice Received" }
    }
    body {
        h2 { +"登録が無事に完了しました。" }
        br
        p { +"支払金額: ${formatYen(invoice.paymentAmount)}" }
        p { +"支払期日: ${formatDateWithSlash(invoice.paymentDueDate)}" }
        p { +"発行日: ${formatDateWithSlash(invoice.issueDate)}" }
        p { +"手数料: ${formatYen(invoice.fee)}" }
        p { +"手数料率: ${invoice.feeRate}" }
        p { +"消費税: ${formatYen(invoice.taxAmount)}" }
        p { +"消費税率: ${invoice.taxRate}" }
        p { +"請求金額: ${formatYen(invoice.totalAmount)}" }
        p { +"作成日時: ${formatDateTimeToString(invoice.createdAt)}" }
        p { +"更新日時: ${formatDateTimeToString(invoice.updatedAt)}" }
        br
        a("/") { +"ホームに戻る" }
    }
}
