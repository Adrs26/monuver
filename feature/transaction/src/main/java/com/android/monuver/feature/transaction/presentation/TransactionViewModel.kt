package com.android.monuver.feature.transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.mapper.toListItemState
import com.android.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.android.monuver.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getDistinctTransactionYearsUseCase: GetDistinctTransactionYearsUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    private val _yearFilterOptions = MutableStateFlow<List<Int>>(emptyList())
    val yearFilterOptions = _yearFilterOptions.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val transactions = combine(
        _searchQuery.onStart { emit(_searchQuery.value) }.debounce(300).distinctUntilChanged(),
        _filterState
    ) { query, filterState ->
        filterState.copy(query = query)
    }.flatMapLatest { filters ->
        getAllTransactionsUseCase(
            query = filters.query,
            type = filters.type,
            month = filters.month,
            year = filters.year,
            scope = viewModelScope
        )
    }.map { pagingData ->
        pagingData.map { transactionState ->
            transactionState.toListItemState()
        }
    }.flowOn(customDispatcher.default)

    fun searchTransactions(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    fun applyFilter(type: Int?, year: Int?, month: Int?) {
        _filterState.update { filterState ->
            filterState.copy(
                type = type,
                year = year,
                month = month
            )
        }

    }

    fun getYearFilterOptions() {
        viewModelScope.launch {
            getDistinctTransactionYearsUseCase().collect { availableYears ->
                _yearFilterOptions.value = availableYears
            }
        }
    }
}

data class FilterState(
    val query: String = "",
    val type: Int? = null,
    val year: Int? = null,
    val month: Int? = null
)