package com.example.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : IdTable<Int>("users") {
    override val id = integer("user_id").entityId()             // PK
    val companyName = varchar("company_name", 255)      // 企業名
    val name = varchar("name", 255)                     // 氏名
    val email = varchar("email", 255)                   // メールアドレス
    val password = varchar("password", 500)             // パスワード
    val createdAt = timestamp("created_at")                     // 作成日時
    val updatedAt = timestamp("updated_at")                     // 更新日時
}

class UsersDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UsersDAO>(UsersTable)

    var companyName by UsersTable.companyName
    var name by UsersTable.name
    var email by UsersTable.email
    var password by UsersTable.password
    var createdAt  by UsersTable.createdAt
    var updatedAt  by UsersTable.updatedAt
}