package com.example.views

import com.example.db.InvoicesDAO
import com.example.models.Invoice
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FlowContent.invoiceList(invoice: InvoicesDAO) {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

    h2 { +"登録が無事に完了しました。" }
    p { +"支払金額: ${invoice.paymentAmount}" }
    p { +"支払期日: ${invoice.paymentDueDate}" }
    p { +"発行日: ${invoice.issueDate}" }
    p { +"手数料: ${invoice.fee}" }
    p { +"手数料率: ${invoice.feeRate}" }
    p { +"消費税: ${invoice.taxAmount}" }
    p { +"消費税率: ${invoice.taxRate}" }
    p { +"請求金額: ${invoice.totalAmount}" }
    p { +"作成日時: ${invoice.createdAt}" }
    p { +"更新日時: ${invoice.updatedAt}" }

    // Convert Instant to LocalDateTime before formatting
//    val dateFromLocalDateTime = invoice.dateFrom.atZone(ZoneId.systemDefault()).toLocalDateTime()
//    val dateToLocalDateTime = invoice.dateTo.atZone(ZoneId.systemDefault()).toLocalDateTime()
//    val dueDateLocalDateTime = invoice.dueDate.atZone(ZoneId.systemDefault()).toLocalDateTime()
//
//    p { +"開始日時: ${dateFromLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
//    p { +"終了日時: ${dateToLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
//    p { +"支払期日: ${dueDateLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}" }
    a("/") { +"ホームに戻る" }
}
