package com.example.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import io.ktor.http.Parameters
import java.time.ZoneId
import kotlin.text.toBigDecimal

data class InvoicesInput(
    val userId: Int,
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal,
    val fee: BigDecimal,
    val feeRate: BigDecimal,
    val taxAmount: BigDecimal,
    val taxRate: BigDecimal,
    val totalAmount: BigDecimal,
    val paymentDueDate: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val FEE_RATE = BigDecimal("0.04")
        private val TAX_RATE = BigDecimal("0.10")

        fun fromParameters(params: Parameters, userId: Int): InvoicesInput {
            val amount = params["paymentAmount"]!!.toBigDecimal()
            val feeTotal = amount.multiply(FEE_RATE)
            val taxTotal = feeTotal.multiply(TAX_RATE)

            return InvoicesInput(
                userId = userId,
                issueDate = LocalDate.now(ZoneId.of("Asia/Tokyo")),
                paymentAmount = amount,
                fee = feeTotal,
                feeRate = FEE_RATE,
                taxAmount = taxTotal,
                taxRate = TAX_RATE,
                totalAmount = amount + feeTotal + taxTotal,
                paymentDueDate = LocalDate.parse(params["paymentDueDate"] ?: throw IllegalArgumentException("paymentDueDate missing"), dateFormatter),
                createdAt = LocalDateTime.now(ZoneId.of("Asia/Tokyo")),
                updatedAt = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
            )
        }
    }
}