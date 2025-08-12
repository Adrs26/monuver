package com.android.monu.presentation.screen.transaction.transactiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.DeleteExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteTransferTransactionUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.navigation.MainTransactionDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteIncomeTransactionUseCase: DeleteIncomeTransactionUseCase,
    private val deleteExpenseTransactionUseCase: DeleteExpenseTransactionUseCase,
    private val deleteTransferTransactionUseCase: DeleteTransferTransactionUseCase
) : ViewModel() {

    val transaction = getTransactionByIdUseCase(
        savedStateHandle.toRoute<MainTransactionDetail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            when {
                transaction.type == TransactionType.INCOME -> {
                    deleteIncomeTransactionUseCase(transaction)
                }
                transaction.type == TransactionType.EXPENSE -> {
                    deleteExpenseTransactionUseCase(transaction)
                }
                transaction.type == TransactionType.TRANSFER &&
                        transaction.childCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                            deleteTransferTransactionUseCase(transaction)
                }
                else -> null
            }
        }
    }
}