package com.android.monu.presentation.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

object NumberFormatHelper {
    fun formatToShortRupiah(amount: Long): String {
        val decimalFormat = DecimalFormat("#.##")

        return when {
            amount >= 1_000_000_000 -> "Rp${decimalFormat.format(amount / 1_000_000_000.0)}M"
            amount >= 1_000_000 -> "Rp${decimalFormat.format(amount / 1_000_000.0)}Jt"
            amount >= 1_000 -> "Rp${decimalFormat.format(amount / 1_000.0)}Rb"
            else -> "Rp$amount"
        }
    }

    fun formatToRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }

    fun formatToPercentageValue(value: Long, total: Long): Long {
        return if (total == 0L) {
            0L
        } else {
            (value.toFloat() / total.toFloat() * 100).roundToLong()
        }
    }
}