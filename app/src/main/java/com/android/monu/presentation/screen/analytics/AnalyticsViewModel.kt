package com.android.monu.presentation.screen.analytics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.android.monu.data.dummy.TransactionsData
import com.android.monu.data.model.MostExpenseCategory
import com.android.monu.data.model.PieData
import com.android.monu.data.model.ScaleLabel
import com.android.monu.data.model.TransactionOverview
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.util.DataHelper
import com.android.monu.util.toHighestRangeValue
import com.android.monu.util.toMonthName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnalyticsViewModel : ViewModel() {
    private val _selectedType = MutableStateFlow("Balance")
    private val _scaleLabels = MutableStateFlow<List<ScaleLabel>>(emptyList())
    private val _monthLabels = MutableStateFlow(DataHelper.monthLabels)
    private val _barDates = MutableStateFlow<List<String>>(emptyList())
    private val _barValues = MutableStateFlow<List<Long>>(emptyList())
    private val _pieValues = MutableStateFlow<List<PieData>>(emptyList())

    val selectedType: StateFlow<String> = _selectedType
    val scaleLabels: StateFlow<List<ScaleLabel>> = _scaleLabels
    val monthLabels: StateFlow<List<Int>> = _monthLabels
    val barDates: StateFlow<List<String>> = _barDates
    val barValues: StateFlow<List<Long>> = _barValues
    val pieValues: StateFlow<List<PieData>> = _pieValues

    init {
        calculateBarValues(TransactionsData.listTransactionsOverview)
        calculatePieValues(TransactionsData.listMostExpenseCategory)
    }

    private fun calculateBarValues(transactionsOverview: List<TransactionOverview>) {
        val tempBarDates = mutableListOf<String>()
        val tempBarValues = mutableListOf<Long>()

        transactionsOverview.forEach {
            tempBarDates.add("${it.month.toMonthName()} ${it.year}")
            tempBarValues.add(it.amount)
        }

        val highestValue = tempBarValues.max().toHighestRangeValue()
        val quarter = highestValue / 4
        val half = highestValue / 2
        val threeQuarter = highestValue - quarter

        _barDates.value = tempBarDates
        _barValues.value = tempBarValues
        _scaleLabels.value = listOf(
            ScaleLabel(highestValue, 1f),
            ScaleLabel(threeQuarter, 0.75f),
            ScaleLabel(half, 0.5f),
            ScaleLabel(quarter, 0.25f),
            ScaleLabel(0, 0f)
        )
    }

    private fun calculatePieValues(mostExpenseCategory: List<MostExpenseCategory>) {
        val pieColors = listOf(Blue, Red, Green, Orange, Color.Black)
        val tempMostExpenseCategory = mutableListOf<PieData>()

        mostExpenseCategory.forEachIndexed { index, data ->
            tempMostExpenseCategory.add(PieData(
                label = data.title,
                value = data.amount,
                color = pieColors[index]
            ))
        }
        _pieValues.value = tempMostExpenseCategory
    }

    fun selectType(type: String) {
        when (type) {
            "Balance" -> calculateBarValues(TransactionsData.listTransactionsOverview)
            "Income" -> calculateBarValues(TransactionsData.listTransactionsOverview2)
            "Expense" -> calculateBarValues(TransactionsData.listTransactionsOverview3)
        }
        _selectedType.value = type
    }
}