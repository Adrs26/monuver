package com.android.monu.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.monu.R
import com.android.monu.ui.theme.ArmyGreen
import com.android.monu.ui.theme.DarkBurgundy
import com.android.monu.ui.theme.DarkGreen
import com.android.monu.ui.theme.DarkIndigo
import com.android.monu.ui.theme.DarkMagenta
import com.android.monu.ui.theme.DarkSlateBlue
import com.android.monu.ui.theme.DeepBlue
import com.android.monu.ui.theme.ElectricBlue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Maroon
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.SaddleBrown
import com.android.monu.ui.theme.SeaGreen
import com.android.monu.ui.theme.SteelNavy

fun Int.toMonthName(): String {
    return when (this) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> ""
    }
}

fun Long.toHighestRangeValue(): Long {
    return when (this) {
        in 0L..1_000_000L -> 1_000_000L
        in 1_000_000L..3_000_000L -> 3_000_000L
        in 3_000_000L..5_000_000L -> 5_000_000L
        in 5_000_000L..7_000_000L -> 7_000_000L
        in 7_000_000L..10_000_000L -> 10_000_000L
        in 10_000_000L..15_000_000L -> 15_000_000L
        in 15_000_000L..20_000_000L -> 20_000_000L
        in 20_000_000L..30_000_000L -> 30_000_000L
        in 30_000_000L..50_000_000L -> 50_000_000L
        in 50_000_000L..100_000_000L -> 100_000_000L
        in 100_000_000L..200_000_000L -> 200_000_000L
        in 200_000_000L..500_000_000L -> 500_000_000L
        else -> 1_000_000_000L
    }
}

fun String.toHighlightColor(): Color {
    return when (this) {
        "Food & Beverages" -> Orange
        "Transportation" -> DarkIndigo
        "Health & Personal Care" -> DarkGreen
        "Bills & Utilities" -> ElectricBlue
        "Education" -> SaddleBrown
        "Entertainment" -> DarkMagenta
        "Shopping" -> Maroon
        "Investment" -> SteelNavy
        "Donation" -> DarkBurgundy
        "Insurance" -> DarkSlateBlue
        "Household" -> SeaGreen
        "Tax" -> DeepBlue
        "Other Expense" -> ArmyGreen
        else -> Green
    }
}

fun String.toHighlightIcon(): Int {
    return when (this) {
        "Food & Beverages" -> R.drawable.ic_expense_food
        "Transportation" -> R.drawable.ic_expense_transportation
        "Health & Personal Care" -> R.drawable.ic_expense_health
        "Bills & Utilities" -> R.drawable.ic_expense_utilities
        "Education" -> R.drawable.ic_expense_education
        "Entertainment" -> R.drawable.ic_expense_entertainment
        "Shopping" -> R.drawable.ic_expense_shopping
        "Investment" -> R.drawable.ic_expense_investment
        "Donation" -> R.drawable.ic_expense_donation
        "Insurance" -> R.drawable.ic_expense_insurance
        "Household" -> R.drawable.ic_expense_household
        "Tax" -> R.drawable.ic_expense_tax
        "Other Expense", "Other Income" -> R.drawable.ic_other
        "Salary" -> R.drawable.ic_income_salary
        "Bonuses" -> R.drawable.ic_income_bonuses
        "Commission" -> R.drawable.ic_income_commission
        else -> 0
    }
}

fun Int.toCategoryName(): Int {
    return when (this) {
        1 -> R.string.food_beverages
        2 -> R.string.transportation
        3 -> R.string.health_personal_care
        4 -> R.string.bills_utilities
        5 -> R.string.education
        6 -> R.string.entertainment
        7 -> R.string.shopping
        8 -> R.string.investment
        9 -> R.string.donation
        10 -> R.string.insurance
        11 -> R.string.household
        12 -> R.string.tax
        13 -> R.string.other_expense
        14 -> R.string.salary
        15 -> R.string.bonuses
        16 -> R.string.commission
        17 -> R.string.other_income
        else -> 0
    }
}

fun Int.toCategoryCode(): Int {
    return when (this) {
        R.string.food_beverages -> 1
        R.string.transportation -> 2
        R.string.health_personal_care -> 3
        R.string.bills_utilities -> 4
        R.string.education -> 5
        R.string.entertainment -> 6
        R.string.shopping -> 7
        R.string.investment -> 8
        R.string.donation -> 9
        R.string.insurance -> 10
        R.string.household -> 11
        R.string.tax -> 12
        R.string.other_expense -> 13
        R.string.salary -> 14
        R.string.bonuses -> 15
        R.string.commission -> 16
        R.string.other_income -> 17
        else -> 0
    }
}

fun Int.toCategoryColor(): Color {
    return when (this) {
        1 -> Orange
        2 -> DarkIndigo
        3 -> DarkGreen
        4 -> ElectricBlue
        5 -> SaddleBrown
        6 -> DarkMagenta
        7 -> Maroon
        8 -> SteelNavy
        9 -> DarkBurgundy
        10 -> DarkSlateBlue
        11 -> SeaGreen
        12 -> DeepBlue
        13 -> ArmyGreen
        14 -> Green
        15 -> Green
        16 -> Green
        17 -> Green
        else -> Green
    }
}

fun Int.toCategoryIcon(): Int {
    return when (this) {
        1 -> R.drawable.ic_expense_food
        2 -> R.drawable.ic_expense_transportation
        3 -> R.drawable.ic_expense_health
        4 -> R.drawable.ic_expense_utilities
        5 -> R.drawable.ic_expense_education
        6 -> R.drawable.ic_expense_entertainment
        7 -> R.drawable.ic_expense_shopping
        8 -> R.drawable.ic_expense_investment
        9 -> R.drawable.ic_expense_donation
        10 -> R.drawable.ic_expense_insurance
        11 -> R.drawable.ic_expense_household
        12 -> R.drawable.ic_expense_tax
        13, 17 -> R.drawable.ic_other
        14 -> R.drawable.ic_income_salary
        15 -> R.drawable.ic_income_bonuses
        16 -> R.drawable.ic_income_commission
        else -> 0
    }
}

fun Int?.toTransactionType(): Int {
    return when (this) {
        null -> R.string.all
        1 -> R.string.income
        2 -> R.string.expense
        else -> 0
    }
}

fun Int.toTransactionTypeCode(): Int? {
    return when (this) {
        R.string.income -> 1
        R.string.expense -> 2
        else -> null
    }
}

fun Int.toFullMonthResourceId(): Int {
    return when (this) {
        1 -> R.string.january
        2 -> R.string.february
        3 -> R.string.march
        4 -> R.string.april
        5 -> R.string.may_full
        6 -> R.string.june
        7 -> R.string.july
        8 -> R.string.august
        9 -> R.string.september
        10 -> R.string.october
        11 -> R.string.november
        12 -> R.string.december
        else -> 0
    }
}

fun Int.toShortMonthResourceId(): Int {
    return when (this) {
        0 -> R.string.all
        1 -> R.string.jan
        2 -> R.string.feb
        3 -> R.string.mar
        4 -> R.string.apr
        5 -> R.string.may_short
        6 -> R.string.jun
        7 -> R.string.jul
        8 -> R.string.aug
        9 -> R.string.sep
        10 -> R.string.oct
        11 -> R.string.nov
        12 -> R.string.dec
        else -> 0
    }
}

fun String.showMessageWithToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 700L,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return this.then(
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = indication
        ) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}