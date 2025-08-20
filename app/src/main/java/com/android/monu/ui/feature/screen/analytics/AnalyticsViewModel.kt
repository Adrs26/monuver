package com.android.monu.ui.feature.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionBalanceSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionCategorySummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionSummaryUseCase
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.threeten.bp.LocalDate

class AnalyticsViewModel(
    getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    private val getTransactionBalanceSummaryUseCase: GetTransactionBalanceSummaryUseCase,
    private val getTransactionCategorySummaryUseCase: GetTransactionCategorySummaryUseCase,
    private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase
) : ViewModel() {

    private val _filterState = MutableStateFlow(AnalyticsFilterState())
    val filterState = _filterState.asStateFlow()

    val yearFilterOptions = getDistinctTransactionYearsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionAmountSummary = _filterState
        .flatMapLatest { filters ->
            getTransactionBalanceSummaryUseCase(filters.month, filters.year)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionCategorySummaries = _filterState
        .flatMapLatest { filters ->
            getTransactionCategorySummaryUseCase(filters.type, filters.month, filters.year)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionDailySummaries = _filterState
        .flatMapLatest { filters ->
            getTransactionSummaryUseCase(filters.month, filters.year, filters.week)
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