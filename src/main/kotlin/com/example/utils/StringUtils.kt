package com.example.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * 金額を「¥」記号を先頭に付け、小数点以下なし（0桁）かつ3桁ごとにカンマ区切りで文字列としてフォーマットする。
 */
fun formatYen(amount: BigDecimal): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.JAPAN).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }
    return formatter.format(amount)
}