package com.android.monu.presentation.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase
) : ViewModel() {

    private val _queryFilter = MutableStateFlow("")
    val queryFilter = _queryFilter.asStateFlow()

    private val _selectedTypeFilter = MutableStateFlow<Int?>(null)
    val selectedTypeFilter = _selectedTypeFilter.asStateFlow()

    private val _selectedYearFilter = MutableStateFlow<Int?>(null)
    val selectedYearFilter = _selectedYearFilter.asStateFlow()

    private val _selectedMonthFilter = MutableStateFlow<Int?>(null)
    val selectedMonthFilter = _selectedMonthFilter.asStateFlow()

    private val _availableTransactionYears = MutableStateFlow<List<Int>>(emptyList())
    val availableTransactionYears = _availableTransactionYears.asStateFlow()

    private val combinedFilters = combine(
        _queryFilter,
        _selectedTypeFilter,
        _selectedYearFilter,
        _selectedMonthFilter
    ) { query, type, year, month ->
        FilterParams(query, type, year, month)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredPagingData = combinedFilters
        .flatMapLatest { filters ->
            getAllTransactionsUseCase.invoke(
                query = filters.query,
                type = filters.type,
                year = filters.year,
                month = filters.month,
                scope = viewModelScope
            )
        }

    fun searchTransactions(query: String) {
        _queryFilter.value = query
    }

    fun selectType(type: Int?) {
        _selectedTypeFilter.value = type
    }

    fun selectYearAndMonth(year: Int?, month: Int?) {
        _selectedYearFilter.value = year
        _selectedMonthFilter.value = month
    }

    fun loadAvailableTransactionYears() {
        viewModelScope.launch {
            getAvailableTransactionYearsUseCase.invoke().collect { availableYears ->
                _availableTransactionYears.value = availableYears
            }
        }
    }
}

data class FilterParams(
    val query: String,
    val type: Int?,
    val year: Int?,
    val month: Int?
)