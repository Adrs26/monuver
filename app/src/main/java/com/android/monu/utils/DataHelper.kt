package com.android.monu.utils

import com.android.monu.R
import com.android.monu.presentation.screen.budgeting.components.DummyBudgeting

object DataHelper {
    val monthLabels = listOf(
        R.string.jan,
        R.string.feb,
        R.string.mar,
        R.string.apr,
        R.string.may_short,
        R.string.jun,
        R.string.jul,
        R.string.aug,
        R.string.sep,
        R.string.oct,
        R.string.nov,
        R.string.dec
    )

    val expenseCategory = listOf(
        R.string.food_beverages,
        R.string.transportation,
        R.string.health_personal_care,
        R.string.bills_utilities,
        R.string.education,
        R.string.entertainment,
        R.string.shopping,
        R.string.investment,
        R.string.donation,
        R.string.insurance,
        R.string.household,
        R.string.tax,
        R.string.other_expense
    )

    val incomeCategory = listOf(
        R.string.salary,
        R.string.bonuses,
        R.string.commission,
        R.string.other_income
    )

    val dummyBudgeting = listOf(
        DummyBudgeting("Budget makan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 250000, 1500000),
        DummyBudgeting("Budget perawatan diri", DateHelper.formatDateToReadable("2025-11-25"), DateHelper.formatDateToReadable("2025-12-25"), 120000, 350000),
        DummyBudgeting("Budget jajan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 200000, 300000),
        DummyBudgeting("Budget internet", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 105000, 105000),
    )
}