package com.android.monu.presentation.screen.transaction.edittransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateIncomeTransactionUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.navigation.MainDetailTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateIncomeTransactionUseCase: UpdateIncomeTransactionUseCase,
    private val updateExpenseTransactionUseCase: UpdateExpenseTransactionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.toRoute<MainDetailTransaction>().id
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    private var originalTransaction: Transaction? = null

    private val _updateResult = MutableStateFlow<Result<Int>?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        if (originalTransaction == null) {
            originalTransaction = _transaction.value
        }

        _transaction.update { transaction ->
            transaction?.copy(parentCategory = parentCategory, childCategory = childCategory)
        }
    }

    fun restoreOriginalTransaction() {
        originalTransaction?.let {
            _transaction.value = it
            originalTransaction = null
        }
    }

    fun updateTransaction(transactionState: EditTransactionContentState) {
        viewModelScope.launch {
            if (
                transactionState.title.isEmpty() ||
                transactionState.date.isEmpty() ||
                transactionState.amount == 0L
            ) {
                _updateResult.value = Result.failure(
                    IllegalArgumentException("Harap lengkapi semua kolom yang tersedia")
                )
                delay(500)
                _updateResult.value = null
            } else {
                val result = when (transactionState.type) {
                    TransactionType.INCOME -> updateIncomeTransactionUseCase(transactionState)
                    TransactionType.EXPENSE -> updateExpenseTransactionUseCase(transactionState)
                    else -> Result.failure(IllegalArgumentException("Tipe transaksi tidak valid"))
                }
                _updateResult.value = result
                delay(500)
                _updateResult.value = null
            }
        }
    }
}