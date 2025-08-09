package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class Invoice(
    val invoiceId: Int,
    val company: String,
    val personInCharge: String,
    @Contextual val loanAmount: BigDecimal,
    @Contextual val dateFrom: LocalDateTime,
    @Contextual val dateTo: LocalDateTime,
    @Contextual val dueDate: LocalDateTime,
    val userId: Int
)