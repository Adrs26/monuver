package com.android.monu.presentation.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetGroupedMonthlyTransactionAmountByParentCategoryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionMonthlyAmountOverviewUseCase
import com.android.monu.domain.usecase.transaction.GetWeeklyTransactionSummaryByDateRangeUseCase
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class AnalyticsViewModel(
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    private val getTransactionMonthlyAmountOverviewUseCase: GetTransactionMonthlyAmountOverviewUseCase,
    private val getGroupedMonthlyTransactionAmountByParentCategoryUseCase: GetGroupedMonthlyTransactionAmountByParentCategoryUseCase,
    private val getWeeklyTransactionSummaryByDateRangeUseCase: GetWeeklyTransactionSummaryByDateRangeUseCase
) : ViewModel() {

    private val _monthFilter = MutableStateFlow<Int>(LocalDate.now().monthValue)
    val monthFilter = _monthFilter.asStateFlow()

    private val _yearFilter = MutableStateFlow<Int>(LocalDate.now().year)
    val yearFilter = _yearFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<Int>(TransactionType.INCOME)
    val typeFilter = _typeFilter.asStateFlow()

    private val _weekFilter = MutableStateFlow<Int>(DateHelper.getCurrentCustomWeekNumber(LocalDate.now().dayOfMonth))
    val weekFilter = _weekFilter.asStateFlow()

    private val combinedFilters = combine(
        _monthFilter, _yearFilter, _typeFilter, _weekFilter
    ) { month, year, type, week ->
        listOf(month, year, type, week)
    }

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions
        .onStart {
            getYearFilterOptions()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionAmountOverview = combinedFilters
        .flatMapLatest { filters ->
            getTransactionMonthlyAmountOverviewUseCase(
                month = filters[0],
                year = filters[1]
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionParentCategorySummary = combinedFilters
        .flatMapLatest { filters ->
            getGroupedMonthlyTransactionAmountByParentCategoryUseCase(
                type = filters[2],
                month = filters[0],
                year = filters[1]
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionWeeklySummary = combinedFilters
        .flatMapLatest { filters ->
            getWeeklyTransactionSummaryByDateRangeUseCase(
                month = filters[0],
                year = filters[1],
                week = filters[3]
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
        _monthFilter.value = month
        changeWeekToAvailableWeek()
    }

    fun changeYearFilter(year: Int) {
        _yearFilter.value = year
        changeWeekToAvailableWeek()
    }

    fun changeTypeFilter(type: Int) {
        _typeFilter.value = type
    }

    fun changeWeekFilter(week: Int) {
        _weekFilter.value = week
    }

    private fun changeWeekToAvailableWeek() {
        val isAvailableWeeks = DateHelper.getWeekOptions(
            month = _monthFilter.value,
            year = _yearFilter.value
        ).contains(_weekFilter.value)

        if (!isAvailableWeeks) {
            _weekFilter.value = 4
        }
    }
}