package com.android.monu.data.dummy

import com.android.monu.data.model.Month
import com.android.monu.data.model.MostExpenseCategory
import com.android.monu.data.model.Transaction
import com.android.monu.data.model.TransactionOverview

object TransactionsData {
    val listRecentItem = listOf(
        Transaction(1, 1, 1),
        Transaction(2, 1, 1),
        Transaction(3, 1, 1)
    )

    val listMonth = listOf(
        Month(1, "January"),
        Month(2, "February"),
        Month(3, "March"),
        Month(4, "April"),
        Month(5, "May"),
        Month(6, "June"),
        Month(7, "July"),
        Month(8, "August"),
        Month(9, "September"),
        Month(10, "October"),
        Month(11, "November"),
        Month(12, "December")
    )

    val listTransactionsOverview = listOf(
        TransactionOverview(1, 1, 2025, 1000000L),
        TransactionOverview(2, 2, 2025, 2500000L),
        TransactionOverview(3, 3, 2025, 3000000L),
        TransactionOverview(4, 4, 2025, 2000000L),
        TransactionOverview(5, 5, 2025, 1750000L),
        TransactionOverview(6, 6, 2025, 3200000L),
        TransactionOverview(7, 7, 2025, 4300000L),
        TransactionOverview(8, 8, 2025, 2700000L),
        TransactionOverview(9, 9, 2025, 4800000L),
        TransactionOverview(10, 10, 2025, 2500000L),
        TransactionOverview(11, 11, 2025, 3000000L),
        TransactionOverview(12, 12, 2025, 4400000L)
    )

    val listTransactionsOverview2 = listOf(
        TransactionOverview(1, 1, 2025, 6000000L),
        TransactionOverview(2, 2, 2025, 3000000L),
        TransactionOverview(3, 3, 2025, 2000000L),
        TransactionOverview(4, 4, 2025, 1000000L),
        TransactionOverview(5, 5, 2025, 4000000L),
        TransactionOverview(6, 6, 2025, 2300000L),
        TransactionOverview(7, 7, 2025, 4700000L),
        TransactionOverview(8, 8, 2025, 1500000L),
        TransactionOverview(9, 9, 2025, 3200000L),
        TransactionOverview(10, 10, 2025, 4300000L),
        TransactionOverview(11, 11, 2025, 1700000L),
        TransactionOverview(12, 12, 2025, 2500000L)
    )

    val listTransactionsOverview3 = listOf(
        TransactionOverview(1, 1, 2025, 3000000L),
        TransactionOverview(2, 2, 2025, 1200000L),
        TransactionOverview(3, 3, 2025, 2500000L),
        TransactionOverview(4, 4, 2025, 2700000L),
        TransactionOverview(5, 5, 2025, 4500000L),
        TransactionOverview(6, 6, 2025, 3300000L),
        TransactionOverview(7, 7, 2025, 3000000L),
        TransactionOverview(8, 8, 2025, 2200000L),
        TransactionOverview(9, 9, 2025, 1800000L),
        TransactionOverview(10, 10, 2025, 4800000L),
        TransactionOverview(11, 11, 2025, 3500000L),
        TransactionOverview(12, 12, 2025, 4700000L)
    )

    val listMostExpenseCategory = listOf(
        MostExpenseCategory(1, "Food & Beverages", 25000000L),
        MostExpenseCategory(4, "Education", 15000000L),
        MostExpenseCategory(3, "Shopping", 12000000L),
        MostExpenseCategory(5, "Health & Personal care", 5000000L),
        MostExpenseCategory(2, "Entertainment", 4000000L),
    )
}