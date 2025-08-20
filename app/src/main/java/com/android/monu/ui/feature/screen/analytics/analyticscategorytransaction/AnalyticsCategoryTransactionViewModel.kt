package com.android.monu.ui.feature.screen.analytics.analyticscategorytransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.transaction.GetAllTransactionsByCategoryUseCase
import com.android.monu.ui.navigation.MainAnalyticsCategoryTransaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AnalyticsCategoryTransactionViewModel(
    savedStateHandle: SavedStateHandle,
    getAllTransactionsByCategoryUseCase: GetAllTransactionsByCategoryUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<MainAnalyticsCategoryTransaction>()

    val category: Int = args.category

    val transactions = getAllTransactionsByCategoryUseCase(
        category = args.category,
        month = args.month,
        year = args.year
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}