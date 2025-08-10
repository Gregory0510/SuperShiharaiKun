package com.example.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class InvoicesOutput(
    val id: Int,
    val userId: Int,
    val issueDate: String,
    @Contextual val paymentAmount: BigDecimal,
    @Contextual val fee: BigDecimal,
    @Contextual val feeRate: BigDecimal,
    @Contextual val taxAmount: BigDecimal,
    @Contextual val taxRate: BigDecimal,
    @Contextual val totalAmount: BigDecimal,
    val paymentDueDate: String,
    val createdAt: String,
    val updatedAt: String
)
