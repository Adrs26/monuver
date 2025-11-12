package com.android.monuver.ui.feature.screen.analytics.analyticsCategoryTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.usecase.transaction.GetAllTransactionsByCategoryUseCase
import com.android.monuver.ui.navigation.AnalyticsCategoryTransaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AnalyticsCategoryTransactionViewModel(
    savedStateHandle: SavedStateHandle,
    getAllTransactionsByCategoryUseCase: GetAllTransactionsByCategoryUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<AnalyticsCategoryTransaction.Main>()

    val category: Int = args.category

    val transactions = getAllTransactionsByCategoryUseCase(
        category = args.category,
        month = args.month,
        year = args.year
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}