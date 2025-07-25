package com.android.monu.presentation.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetGroupedMonthlyTransactionAmountByParentCategoryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionMonthlyAmountSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetWeeklyTransactionSummaryByDateRangeUseCase
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class AnalyticsViewModel(
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    private val getTransactionMonthlyAmountSummaryUseCase: GetTransactionMonthlyAmountSummaryUseCase,
    private val getGroupedMonthlyTransactionAmountByParentCategoryUseCase: GetGroupedMonthlyTransactionAmountByParentCategoryUseCase,
    private val getWeeklyTransactionSummaryByDateRangeUseCase: GetWeeklyTransactionSummaryByDateRangeUseCase
) : ViewModel() {

    private val _filterState = MutableStateFlow(AnalyticsFilterState())
    val filterState = _filterState.asStateFlow()

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions
        .onStart {
            getYearFilterOptions()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionAmountSummary = _filterState
        .flatMapLatest { filters ->
            getTransactionMonthlyAmountSummaryUseCase(
                month = filters.month,
                year = filters.year
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionParentCategorySummary = _filterState
        .flatMapLatest { filters ->
            getGroupedMonthlyTransactionAmountByParentCategoryUseCase(
                type = filters.type,
                month = filters.month,
                year = filters.year
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionWeeklySummary = _filterState
        .flatMapLatest { filters ->
            getWeeklyTransactionSummaryByDateRangeUseCase(
                month = filters.month,
                year = filters.year,
                week = filters.week
            )
        }

    private fun getYearFilterOptions() {
        viewModelScope.launch {
            getDistinctTransactionYearsUseCase().collect { availableYears ->
                _yearFilterOptions.value = availableYears
            }
        }
    }

    fun changeMonthFilter(month: Int) {
        _filterState.update { it.copy(month = month) }
        changeWeekToAvailableWeek()
    }

    fun changeYearFilter(year: Int) {
        _filterState.update { it.copy(year = year) }
        changeWeekToAvailableWeek()
    }

    fun changeTypeFilter(type: Int) {
        _filterState.update { it.copy(type = type) }
    }

    fun changeWeekFilter(week: Int) {
        _filterState.update { it.copy(week = week) }
    }

    private fun changeWeekToAvailableWeek() {
        val isAvailableWeeks = DateHelper.getWeekOptions(
            month = _filterState.value.month,
            year = _filterState.value.year
        ).contains(_filterState.value.week)

        if (!isAvailableWeeks) {
            _filterState.update { it.copy(week = 4) }
        }
    }
}

data class AnalyticsFilterState(
    val month: Int = LocalDate.now().monthValue,
    val year: Int = LocalDate.now().year,
    val type: Int = TransactionType.INCOME,
    val week: Int = DateHelper.getCurrentCustomWeekNumber(LocalDate.now().dayOfMonth)
)