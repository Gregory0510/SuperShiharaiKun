package com.example.db

import com.example.models.Invoice
import java.math.BigDecimal
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDateTime


fun fetchActiveDueDateInRange( connection: Connection, from: LocalDateTime, to: LocalDateTime, userId: Int): List<Invoice> {
    val invoices = mutableListOf<Invoice>()

    val sql = """
        SELECT invoice_id, company, person_in_charge, loan_amount, date_from, date_to, due_date, user_id 
        FROM Invoice
        WHERE due_date BETWEEN ? AND ? AND user_id = ?
    """.trimIndent()

    val stmt = connection.prepareStatement(sql)
    stmt.setTimestamp(1, Timestamp.valueOf(from))
    stmt.setTimestamp(2, Timestamp.valueOf(to))
    stmt.setInt(3, userId)

    val rs = stmt.executeQuery()

    while (rs.next()) {
        invoices.add(
            Invoice(
                invoiceId = rs.getInt("invoice_id"),
                company = rs.getString("company"),
                personInCharge = rs.getString("person_in_charge"),
                loanAmount = rs.getBigDecimal("loan_amount"),
                dateFrom = rs.getTimestamp("date_from").toLocalDateTime(),
                dateTo = rs.getTimestamp("date_to").toLocalDateTime(),
                dueDate = rs.getTimestamp("due_date").toLocalDateTime(),
                userId = rs.getInt("user_id")
            )
        )
    }

    rs.close()
    stmt.close()

    return invoices
}

fun insertInvoice(connection: Connection, company: String, personInCharge: String, loanAmount: BigDecimal, dateFrom: LocalDateTime, dateTo: LocalDateTime, dueDate: LocalDateTime, userId: Int): Invoice? {
    val sql = "INSERT INTO Invoice (company, person_in_charge, loan_amount, date_from, date_to, due_date, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)"
    val preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

    preparedStatement.setString(1, company)
    preparedStatement.setString(2, personInCharge)
    preparedStatement.setBigDecimal(3, loanAmount)
    preparedStatement.setTimestamp(4, Timestamp.valueOf(dateFrom))
    preparedStatement.setTimestamp(5, Timestamp.valueOf(dateTo))
    preparedStatement.setTimestamp(6, Timestamp.valueOf(dueDate))
    preparedStatement.setInt(7, userId)

    val affectedRows = preparedStatement.executeUpdate()
    if (affectedRows == 0) return null

    val generatedKeys = preparedStatement.generatedKeys
    val invoiceId = if (generatedKeys.next()) generatedKeys.getInt(1) else 0

    preparedStatement.close()

    return Invoice(invoiceId, company, personInCharge, loanAmount, dateFrom, dateTo, dueDate, userId)
}