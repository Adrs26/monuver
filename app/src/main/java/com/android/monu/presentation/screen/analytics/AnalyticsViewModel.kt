package com.android.monu.presentation.screen.analytics

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.dummy.TransactionsData
import com.android.monu.data.model.MostExpenseCategory
import com.android.monu.data.model.PieData
import com.android.monu.domain.model.AverageTransactionAmount
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.GetAverageTransactionAmountUseCase
import com.android.monu.domain.usecase.GetMonthlyTransactionOverviewUseCase
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.util.toHighestRangeValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val getAverageTransactionAmountUseCase: GetAverageTransactionAmountUseCase,
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase,
    private val getMonthlyTransactionOverviewUseCase: GetMonthlyTransactionOverviewUseCase
) : ViewModel() {

    private val _pieValues = MutableStateFlow<List<PieData>>(emptyList())
    val pieValues: StateFlow<List<PieData>> = _pieValues

    private val _averageTransactionAmount = MutableStateFlow<AverageTransactionAmount?>(null)
    val averageTransactionAmount = _averageTransactionAmount
        .onStart {
            getAverageTransactionAmount()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _barChartSelectedTypeFilter = MutableStateFlow(3)
    val barChartSelectedTypeFilter = _barChartSelectedTypeFilter.asStateFlow()

    private val _barChartSelectedYearFilter = MutableStateFlow<Int>(
        Calendar.getInstance().get(Calendar.YEAR)
    )
    val barChartSelectedYearFilter = _barChartSelectedYearFilter.asStateFlow()

    private val _barChartScaleLabels = MutableStateFlow<List<BarChartScaleLabel>>(emptyList())
    val barChartScaleLabels = _barChartScaleLabels.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionsOverview = combine(
        _barChartSelectedTypeFilter,
        _barChartSelectedYearFilter
    ) { type, year -> type to year }.flatMapLatest { (type, year) ->
        getMonthlyTransactionOverviewUseCase.invoke(type, year)
    }.onEach {
        val highestValue = it.maxOfOrNull { it.amount } ?: 0
        calculateBarScaleLabels(highestValue.toHighestRangeValue())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _pieChartSelectedYearFilter = MutableStateFlow<Int>(
        Calendar.getInstance().get(Calendar.YEAR)
    )
    val pieChartSelectedYearFilter = _pieChartSelectedYearFilter.asStateFlow()

    private val _availableTransactionYears = MutableStateFlow<List<Int>>(emptyList())
    val availableTransactionYears = _availableTransactionYears.asStateFlow()

    init {
        calculatePieValues(TransactionsData.listMostExpenseCategory)
    }

    private fun getAverageTransactionAmount() {
        viewModelScope.launch {
            getAverageTransactionAmountUseCase.invoke().collect { averageTransactionAmount ->
                _averageTransactionAmount.value = averageTransactionAmount
            }
        }
    }

    fun loadAvailableTransactionYears() {
        viewModelScope.launch {
            getAvailableTransactionYearsUseCase.invoke().collect { availableYears ->
                _availableTransactionYears.value = availableYears
            }
        }
    }

    fun calculateBarScaleLabels(highestValue: Long) {
        val highestValue = highestValue
        val quarter = highestValue / 4
        val half = highestValue / 2
        val threeQuarter = highestValue - quarter

        _barChartScaleLabels.value = listOf(
            BarChartScaleLabel(highestValue, 1f),
            BarChartScaleLabel(threeQuarter, 0.75f),
            BarChartScaleLabel(half, 0.5f),
            BarChartScaleLabel(quarter, 0.25f),
            BarChartScaleLabel(0, 0f)
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

    fun selectType(type: Int) {
        _barChartSelectedTypeFilter.value = type
    }

    fun selectBarChartYear(year: Int) {
        _barChartSelectedYearFilter.value = year
    }

    fun selectPieChartYear(year: Int) {
        _pieChartSelectedYearFilter.value = year
    }
}