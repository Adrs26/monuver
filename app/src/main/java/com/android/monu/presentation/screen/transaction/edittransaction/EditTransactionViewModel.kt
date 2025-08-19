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
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.navigation.EditTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateIncomeTransactionUseCase: UpdateIncomeTransactionUseCase,
    private val updateExpenseTransactionUseCase: UpdateExpenseTransactionUseCase,
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.toRoute<EditTransaction>().id
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _initialTransaction = MutableStateFlow<Transaction?>(null)
    val initialTransaction: StateFlow<Transaction?> = _initialTransaction

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _initialTransaction.value = transaction
                _transaction.value = transaction
            }
        }
    }

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transaction.update { transaction ->
            transaction?.copy(parentCategory = parentCategory, childCategory = childCategory)
        }
    }

    fun restoreOriginalTransaction() {
        _initialTransaction.value.let {
            _transaction.value = it
        }
    }

    fun updateTransaction(transactionState: EditTransactionContentState) {
        viewModelScope.launch {
            val result = when (transactionState.type) {
                TransactionType.INCOME -> updateIncomeTransactionUseCase(transactionState)
                TransactionType.EXPENSE -> updateExpenseTransactionUseCase(transactionState)
                else -> null
            }
            _updateResult.value = result
            delay(500)
            _updateResult.value = null
        }
    }
}