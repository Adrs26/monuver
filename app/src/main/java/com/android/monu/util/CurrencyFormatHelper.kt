package com.android.monu.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatHelper {
    fun rupiahToShortFormat(amount: Long): String {
        val decimalFormat = DecimalFormat("#.##")

        return when {
            amount >= 1_000_000_000 -> "Rp${decimalFormat.format(amount / 1_000_000_000.0)}M"
            amount >= 1_000_000 -> "Rp${decimalFormat.format(amount / 1_000_000.0)}Jt"
            amount >= 1_000 -> "Rp${decimalFormat.format(amount / 1_000.0)}K"
            else -> "Rp$amount"
        }
    }

    fun formatToRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }

    fun formatToThousandDivider(value: Long): String {
        return String.format(Locale("in", "ID"), "%,d", value)
            .replace(',', '.')
    }
}