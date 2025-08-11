package com.example.dao

import com.example.dto.InvoicesOutput
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp

object InvoicesTable : IdTable<Int>("Invoices") {
    override val id = integer("invoice_id").entityId()                      // PK
    val userId = integer("user_id")                                         // 企業ID
    val issueDate = date("issue_date")                                      // 発行日
    val paymentAmount = decimal("payment_amount", 15, 2)   // 支払金額
    val fee = decimal("fee", 15, 2)                        // 手数料
    val feeRate = decimal("fee_rate", 5, 2)                // 手数料率
    val taxAmount = decimal("tax_amount", 15, 2)           // 消費税
    val taxRate = decimal("tax_rate", 5, 2)                // 消費税率
    val totalAmount = decimal("total_amount", 15, 2)       // 請求金額
    val paymentDueDate = date("payment_due_date")                           // 支払期日
    val createdAt = timestamp("created_at")                                 // 作成日時
    val updatedAt = timestamp("updated_at")                                 // 更新日時
}

class InvoicesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InvoicesDAO>(InvoicesTable)

    var userId by InvoicesTable.userId
    var issueDate by InvoicesTable.issueDate
    var paymentAmount by InvoicesTable.paymentAmount
    var fee  by InvoicesTable.fee
    var feeRate  by InvoicesTable.feeRate
    var taxAmount  by InvoicesTable.taxAmount
    var taxRate  by InvoicesTable.taxRate
    var totalAmount  by InvoicesTable.totalAmount
    var paymentDueDate  by InvoicesTable.paymentDueDate
    var createdAt  by InvoicesTable.createdAt
    var updatedAt  by InvoicesTable.updatedAt
}

fun InvoicesDAO.toDTO(): InvoicesOutput {
    return InvoicesOutput(
        id = this.id.value,
        userId = this.userId,
        issueDate = this.issueDate.toString(),
        paymentAmount = this.paymentAmount,
        fee = this.fee,
        feeRate = this.feeRate,
        taxAmount = this.taxAmount,
        taxRate = this.taxRate,
        totalAmount = this.totalAmount,
        paymentDueDate = this.paymentDueDate.toString(),
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}