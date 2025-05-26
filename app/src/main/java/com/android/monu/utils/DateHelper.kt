package com.android.monu.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

object DateHelper {
    fun formatDateToReadable(inputDate: String): String {
        val localeIndonesia = Locale("id", "ID")
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", localeIndonesia)
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", localeIndonesia)

        return try {
            val date = LocalDate.parse(inputDate, inputFormatter)
            date.format(outputFormatter)
        } catch (_: Exception) {
            inputDate
        }
    }

    fun getMonthAndYear(inputDate: String): Pair<Int, Int> {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("id", "ID"))
            val date = LocalDate.parse(inputDate, formatter)
            Pair(date.monthValue, date.year)
        } catch (_: Exception) {
            Pair(0, 0)
        }
    }
}