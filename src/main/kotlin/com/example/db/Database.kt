package com.example.db

import java.sql.Connection
import java.sql.DriverManager

fun connectToDatabase(): Connection {
    val jdbcUrl = "jdbc:mysql://localhost:3306/shiharaikun?useSSL=false&serverTimezone=Asia/Tokyo"
    val username = "root"
    val password = "root"

    return DriverManager.getConnection(jdbcUrl, username, password)
}