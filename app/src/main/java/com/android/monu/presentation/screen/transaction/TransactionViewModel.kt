package com.android.monu.presentation.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetAvailableTransactionYearsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase
) : ViewModel() {

    private val _queryFilter = MutableStateFlow("")
    val queryFilter = _queryFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<Int?>(null)
    val typeFilter = _typeFilter.asStateFlow()

    private val _yearFilter = MutableStateFlow<Int?>(null)
    val yearFilter = _yearFilter.asStateFlow()

    private val _monthFilter = MutableStateFlow<Int?>(null)
    val monthFilter = _monthFilter.asStateFlow()

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions.asStateFlow()

    private val combinedFilters = combine(
        _queryFilter, _typeFilter, _yearFilter, _monthFilter
    ) { query, type, year, month ->
        listOf(query, type, year, month)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = combinedFilters
        .flatMapLatest { filters ->
            getAllTransactionsUseCase(
                query = filters[0].toString(),
                type = filters[1] as Int?,
                year = filters[2] as Int?,
                month = filters[3] as Int?,
                scope = viewModelScope
            )
        }

    fun searchTransactions(query: String) {
        _queryFilter.value = query
    }

    fun applyFilter(type: Int?, year: Int?, month: Int?) {
        _typeFilter.value = type
        _yearFilter.value = year
        _monthFilter.value = month
    }

    fun getYearFilterOptions() {
        viewModelScope.launch {
            getAvailableTransactionYearsUseCase().collect { availableYears ->
                _yearFilterOptions.value = availableYears
            }
        }
    }
}