package com.android.monu.ui.feature.screen.transaction.transactionDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.DeleteExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteIncomeTransactionUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.ui.feature.utils.TransactionType
import com.android.monu.ui.navigation.TransactionDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteIncomeTransactionUseCase: DeleteIncomeTransactionUseCase,
    private val deleteExpenseTransactionUseCase: DeleteExpenseTransactionUseCase
) : ViewModel() {

    val transaction = getTransactionByIdUseCase(
        savedStateHandle.toRoute<TransactionDetail.Main>().transactionId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            when (transaction.type) {
                TransactionType.INCOME -> {
                    deleteIncomeTransactionUseCase(transaction)
                }
                TransactionType.EXPENSE -> {
                    deleteExpenseTransactionUseCase(transaction)
                }
                else -> null
            }
        }
    }
}