package com.example.db

import java.sql.Connection
import java.sql.Timestamp
import java.time.LocalDateTime
import com.example.models.UserProfile
import org.mindrot.jbcrypt.BCrypt

fun fetchAllUsers(connection: Connection): List<UserProfile> {
    val users = mutableListOf<UserProfile>()
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery("SELECT user_id, name, email, age, password, last_update_date FROM UserProfile")

    while (rs.next()) {
        users.add(
            UserProfile(
                userId = rs.getInt("user_id"),
                name = rs.getString("name"),
                email = rs.getString("email"),
                age = rs.getInt("age").takeIf { !rs.wasNull() } ?: 0,
                password = rs.getString("password"),
                lastUpdateDate = rs.getTimestamp("last_update_date").toLocalDateTime()
            )
        )
    }

    rs.close()
    stmt.close()

    return users
}

fun insertUserProfile(connection: Connection, name: String, email: String, age: Int, password: String, lastUpdateDate: LocalDateTime): UserProfile? {
    val sql = "INSERT INTO UserProfile (name, email, age, password, last_update_date) VALUES (?, ?, ?, ?, ?)"
    val preparedStatement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)

    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    preparedStatement.setString(1, name)
    preparedStatement.setString(2, email)
    preparedStatement.setInt(3, age)
    preparedStatement.setString(4, hashedPassword)
    preparedStatement.setTimestamp(5, Timestamp.valueOf(lastUpdateDate))

    val affectedRows = preparedStatement.executeUpdate()
    if (affectedRows == 0) return null

    val generatedKeys = preparedStatement.generatedKeys
    val userId = if (generatedKeys.next()) generatedKeys.getInt(1) else 0

    preparedStatement.close()

    return UserProfile(userId, name, email, age, password, lastUpdateDate)
}