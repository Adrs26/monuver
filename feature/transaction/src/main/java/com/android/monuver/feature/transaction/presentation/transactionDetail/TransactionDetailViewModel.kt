package com.android.monuver.feature.transaction.presentation.transactionDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.feature.transaction.domain.usecase.DeleteExpenseTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.DeleteIncomeTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class TransactionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteIncomeTransactionUseCase: DeleteIncomeTransactionUseCase,
    private val deleteExpenseTransactionUseCase: DeleteExpenseTransactionUseCase
) : ViewModel() {

    val transactionState = getTransactionByIdUseCase(
        savedStateHandle.toRoute<TransactionDetail.Main>().transactionId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun deleteTransaction(transactionState: TransactionState) {
        viewModelScope.launch {
            when (transactionState.type) {
                TransactionType.INCOME -> {
                    deleteIncomeTransactionUseCase(transactionState)
                }
                TransactionType.EXPENSE -> {
                    deleteExpenseTransactionUseCase(transactionState)
                }
                else -> {}
            }
        }
    }
}