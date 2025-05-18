package com.android.monu.data.model

import androidx.compose.ui.graphics.Color

data class Transaction(
    val id: Int,
    val date: Int,
    val title: Int
)

data class TransactionOverview(
    val id: Int,
    val month: Int,
    val year: Int,
    val amount: Long
)

data class MostExpenseCategory(
    val id: Int,
    val title: String,
    val amount: Long
)

data class PieData(
    val label: String,
    val value: Long,
    val color: Color
)
