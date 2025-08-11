package com.example.repository

import com.example.dao.UsersDAO
import com.example.dao.UsersTable
import com.example.dto.UsersInput
import com.example.models.Users
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.ZoneId

/**
 * メールアドレスでユーザー情報を取得する
 */
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

/**
 * ユーザー一覧の取得
 */
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

/**
 * ユーザーの登録
 */
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