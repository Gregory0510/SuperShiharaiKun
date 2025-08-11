package com.example.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Instantを"yyyy/MM/dd HH:mm:ss"に変換
 */
fun formatDateTimeToString(dateTime: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val dateTimeLocalDateTime = dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime()
    return dateTimeLocalDateTime.format(formatter)
}

/**
 * LocalDateTimeを"yyyy/MM/dd HH:mm:ss"に変換
 */
fun formatDateTimeToString(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    return dateTime.format(formatter)
}

/**
 * LocalDateを"yyyy/MM/dd"に変換
 */
fun formatDateWithSlash(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    return date.format(formatter)
}