package com.android.monuver.feature.analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.data.datastore.UserPreferences
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionBalanceSummaryUseCase
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionCategorySummaryUseCase
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionSummaryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AnalyticsViewModel(
    private val getTransactionBalanceSummaryUseCase: GetTransactionBalanceSummaryUseCase,
    private val getTransactionCategorySummaryUseCase: GetTransactionCategorySummaryUseCase,
    private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
    preferences: UserPreferences,
    getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
) : ViewModel() {

    val themeState = preferences.themeState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeState.System
        )

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

data class AnalyticsFilterState @OptIn(ExperimentalTime::class) constructor(
    val month: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.month.number,
    val year: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.year,
    val type: Int = TransactionType.EXPENSE,
    val week: Int = DateHelper.getCurrentWeekNumber(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.day
    )
)