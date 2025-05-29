package com.android.monu.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
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

    fun formatToConciseRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val symbols = DecimalFormatSymbols(localeID).apply {
            decimalSeparator = ','
            groupingSeparator = '.'
        }

        fun format(value: Double): String {
            return if (value % 1 == 0.0) {
                DecimalFormat("#,##0", symbols).format(value)
            } else {
                DecimalFormat("#,##0.00", symbols).format(value)
            }
        }

        return when {
            amount >= 1_000_000_000_000 -> {
                val value = amount / 1_000_000_000_000.0
                "Rp${format(value)} Triliun"
            }
            amount >= 1_000_000_000 -> {
                val value = amount / 1_000_000_000.0
                "Rp${format(value)} Miliar"
            }
            amount >= 1_000_000 -> {
                val value = amount / 1_000_000.0
                "Rp${format(value)} Juta"
            }
            amount >= 1_000 -> {
                val value = amount / 1_000.0
                "Rp${format(value)} Ribu"
            }
            else -> "Rp${DecimalFormat("#,##0", symbols).format(amount)}"
        }
    }


    fun formatToThousandDivider(value: Long): String {
        return String.format(Locale("in", "ID"), "%,d", value).replace(',', '.')
    }

    fun formatToPercentageValue(value: Long, total: Long): Long {
        return (value.toFloat() / total.toFloat() * 100).roundToLong()
    }
}