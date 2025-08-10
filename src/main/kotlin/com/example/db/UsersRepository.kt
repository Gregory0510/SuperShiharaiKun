package com.example.db

import com.example.dto.UsersInput
import com.example.models.Users
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.ZoneId

fun fetchUserByEmail(email: String): Users? = transaction {
    UsersDAO.find {
        UsersTable.email eq email
    }.firstOrNull()?.let { dao ->
        Users(
            userId = dao.id.value,
            companyName = dao.companyName,
            name = dao.name,
            email = dao.email,
            password = dao.password,
            createdAt = dao.createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime(),
            updatedAt = dao.updatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime()
        )
    }
}

fun fetchAllUsers(): List<Users> = transaction {
    UsersDAO.all().map { dao ->
        Users(
            userId = dao.id.value,
            companyName = dao.companyName,
            name = dao.name,
            email = dao.email,
            password = dao.password,
            createdAt = dao.createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime(),
            updatedAt = dao.updatedAt.atZone(ZoneId.systemDefault()).toLocalDateTime()
        )
    }
}

suspend fun insertUserProfile(input: UsersInput): UsersDAO {
    return newSuspendedTransaction {
        UsersDAO.new {
            this.companyName = input.companyName
            this.name = input.name
            this.email = input.email
            this.password = input.password
            this.createdAt = input.createdAt.atZone(ZoneId.of("Asia/Tokyo")).toInstant()
            this.updatedAt = input.updatedAt.atZone(ZoneId.of("Asia/Tokyo")).toInstant()
        }
    }
}