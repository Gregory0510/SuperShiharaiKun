package com.example.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.ZoneId
import com.example.dto.InvoicesInput

fun fetchActiveDueDateInRange(from: LocalDate, to: LocalDate, userId: Int): List<InvoicesDAO> = transaction {
    InvoicesDAO.find {
        (InvoicesTable.paymentDueDate.between(from, to)) and (InvoicesTable.userId eq userId)
    }.toList()
}

suspend fun insertInvoice(input: InvoicesInput): InvoicesDAO {
    return newSuspendedTransaction(Dispatchers.IO) {
        InvoicesDAO.new {
            this.userId = input.userId
            this.issueDate = input.issueDate.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant()
            this.paymentAmount = input.paymentAmount
            this.fee = input.fee
            this.feeRate = input.feeRate
            this.taxAmount = input.taxAmount
            this.taxRate = input.taxRate
            this.totalAmount = input.totalAmount
            this.paymentDueDate = input.paymentDueDate
            this.createdAt = input.createdAt.atZone(ZoneId.of("Asia/Tokyo")).toInstant()
            this.updatedAt = input.updatedAt.atZone(ZoneId.of("Asia/Tokyo")).toInstant()
        }
    }
}