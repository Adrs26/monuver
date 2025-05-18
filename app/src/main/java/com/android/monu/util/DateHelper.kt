package com.android.monu.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateHelper {
    fun formatDateToReadable(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    fun getMonthAndYear(dateString: String): Pair<Int, Int> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(dateString) ?: return Pair(0, 0)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        return Pair(month, year)
    }
}