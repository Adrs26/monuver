package com.android.monu.ui.feature.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase
) : ViewModel() {

    private val _filterState = MutableStateFlow(TransactionFilterState())
    val filterState = _filterState.asStateFlow()

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = _filterState
        .flatMapLatest { filters ->
            getAllTransactionsUseCase(
                filters.query, filters.type, filters.year, filters.month, viewModelScope
            )
        }

    fun searchTransactions(query: String) {
        _filterState.update { it.copy(query = query) }
    }

    fun applyFilter(type: Int?, year: Int?, month: Int?) {
        _filterState.update { it.copy(type = type, year = year, month = month) }

    }

    fun getYearFilterOptions() {
        viewModelScope.launch {
            getDistinctTransactionYearsUseCase().collect { availableYears ->
                _yearFilterOptions.value = availableYears
            }
        }
    }
}

data class TransactionFilterState(
    val query: String = "",
    val type: Int? = null,
    val year: Int? = null,
    val month: Int? = null
)