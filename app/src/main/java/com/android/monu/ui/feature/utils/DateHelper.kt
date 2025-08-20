package com.android.monu.ui.feature.utils

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAdjusters
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

    fun getCurrentCustomWeekNumber(dayOfMonth: Int): Int {
        return when (dayOfMonth) {
            in 1..7 -> 1
            in 8..14 -> 2
            in 15..21 -> 3
            in 22..28 -> 4
            else -> 5
        }
    }

    fun getWeekOptions(month: Int, year: Int): List<Int> {
        val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

        val weekOptions = mutableListOf(1, 2, 3, 4)

        if (daysInMonth > 28) {
            weekOptions.add(5)
        }

        return weekOptions
    }

    fun formatToWeekString(weekNumber: Int): String {
        return when (weekNumber) {
            1 -> "Minggu ke-1"
            2 -> "Minggu ke-2"
            3 -> "Minggu ke-3"
            4 -> "Minggu ke-4"
            5 -> "Minggu ke-5"
            else -> ""
        }
    }

    fun getDateRangeForWeek(week: Int, month: Int, year: Int): Pair<LocalDate, LocalDate> {
        val startDay = (week - 1) * 7 + 1
        val startDate = LocalDate.of(year, month, startDay)

        val endDay = minOf(startDay + 6, startDate.lengthOfMonth())
        val endDate = LocalDate.of(year, month, endDay)

        return startDate to endDate
    }

    fun formatToBarChartDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM")

        val date = LocalDate.parse(inputDate, inputFormatter)
        return date.format(outputFormatter)
    }

    fun getFirstDayOfCurrentMonth(): String {
        val today = LocalDate.now()
        val yearMonth = YearMonth.from(today)
        return yearMonth.atDay(1).toString()
    }

    fun getLastDayOfCurrentMonth(): String {
        val today = LocalDate.now()
        val yearMonth = YearMonth.from(today)
        return yearMonth.atEndOfMonth().toString()
    }

    fun getFirstDayOfCurrentWeek(): String {
        val today = LocalDate.now()
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
    }

    fun getLastDayOfCurrentWeek(): String {
        val today = LocalDate.now()
        return today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()
    }

    fun formatToShortDate(inputDate: String): String {
        val parsedDate = LocalDate.parse(inputDate)
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
        return parsedDate.format(formatter)
    }

    fun getNextMonthSameDate(inputDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(inputDate, formatter)

        val nextMonthDate = date.plusMonths(1)
        return nextMonthDate.format(formatter)
    }

    fun getNextWeekSameDate(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateStr, formatter)

        val nextWeekDate = date.plusWeeks(1)
        return nextWeekDate.format(formatter)
    }
}