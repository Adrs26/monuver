package com.android.monuver.feature.analytics.presentation.analyticsCategoryTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.mapper.toListItemState
import com.android.monuver.core.presentation.navigation.AnalyticsCategoryTransaction
import com.android.monuver.feature.analytics.domain.usecase.GetAllTransactionsByCategoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AnalyticsCategoryTransactionViewModel(
    savedStateHandle: SavedStateHandle,
    getAllTransactionsByCategoryUseCase: GetAllTransactionsByCategoryUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    private val args = savedStateHandle.toRoute<AnalyticsCategoryTransaction.Main>()

    val category: Int = args.category

    val transactions = getAllTransactionsByCategoryUseCase(
        category = args.category,
        month = args.month,
        year = args.year
    ).map { transactions ->
        transactions.map { transactionState ->
            transactionState.toListItemState()
        }
    }.flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}