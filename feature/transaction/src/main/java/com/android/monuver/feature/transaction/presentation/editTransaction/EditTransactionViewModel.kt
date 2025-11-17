package com.android.monuver.feature.transaction.presentation.editTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.feature.transaction.domain.model.EditTransactionState
import com.android.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import com.android.monuver.feature.transaction.domain.usecase.UpdateExpenseTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.UpdateIncomeTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EditTransactionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateIncomeTransactionUseCase: UpdateIncomeTransactionUseCase,
    private val updateExpenseTransactionUseCase: UpdateExpenseTransactionUseCase,
) : ViewModel() {

    private val _transactionState = MutableStateFlow<TransactionState?>(null)
    val transaction = _transactionState
        .onStart {
            val id = savedStateHandle.toRoute<TransactionDetail.Edit>().transactionId
            getTransactionById(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _initialTransactionState = MutableStateFlow<TransactionState?>(null)
    val initialTransaction = _initialTransactionState.asStateFlow()

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _initialTransactionState.value = transaction
                _transactionState.value = transaction
            }
        }
    }

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transactionState.update { transaction ->
            transaction?.copy(
                parentCategory = parentCategory,
                childCategory = childCategory
            )
        }
    }

    fun restoreOriginalTransaction() {
        _initialTransactionState.value.let { initialTransaction ->
            _transactionState.value = initialTransaction
        }
    }

    fun updateTransaction(transactionState: EditTransactionState) {
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