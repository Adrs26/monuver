package com.android.monu.presentation.screen.transaction.detailtransaction

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
import com.android.monu.ui.navigation.MainDetailTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailTransactionViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteIncomeTransactionUseCase: DeleteIncomeTransactionUseCase,
    private val deleteExpenseTransactionUseCase: DeleteExpenseTransactionUseCase,
    private val deleteTransferTransactionUseCase: DeleteTransferTransactionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.toRoute<MainDetailTransaction>().id
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    private val _deleteResult = MutableStateFlow<Result<Int>?>(null)
    val deleteResult = _deleteResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val result = when {
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
                else -> Result.failure(IllegalArgumentException("Tipe transaksi tidak valid"))
            }

            _deleteResult.value = result
            delay(500)
            _deleteResult.value = null
        }
    }
}