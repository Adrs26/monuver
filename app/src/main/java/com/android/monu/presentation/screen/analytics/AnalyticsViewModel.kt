package com.android.monu.presentation.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionAmountOverviewUseCase
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
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase,
    private val getTransactionAmountOverviewUseCase: GetTransactionAmountOverviewUseCase
) : ViewModel() {

    private val _monthFilter = MutableStateFlow<Int>(LocalDate.now().monthValue)
    val monthFilter = _monthFilter.asStateFlow()

    private val _yearFilter = MutableStateFlow<Int>(LocalDate.now().year)
    val yearFilter = _yearFilter.asStateFlow()

    private val combinedFilters = combine(
        _monthFilter, _yearFilter
    ) { month, year ->
        listOf(month, year)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionAmountOverview = combinedFilters
        .flatMapLatest { filters ->
            getTransactionAmountOverviewUseCase(
                month = filters[0],
                year = filters[1]
            )
        }

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions
        .onStart {
            getYearFilterOptions()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getYearFilterOptions() {
        viewModelScope.launch {
            getAvailableTransactionYearsUseCase().collect { availableYears ->
                _yearFilterOptions.value = availableYears
            }
        }
    }

    fun changeMonthFilter(month: Int) {
        _monthFilter.value = month
    }

    fun changeYearFilter(year: Int) {
        _yearFilter.value = year
    }
}