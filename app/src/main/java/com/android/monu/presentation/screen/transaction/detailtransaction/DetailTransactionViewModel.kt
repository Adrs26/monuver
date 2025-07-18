package com.android.monu.presentation.screen.transaction.detailtransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.DeleteTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.ui.navigation.MainDetailTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailTransactionViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteTransactionAndAdjustAccountBalanceUseCase: DeleteTransactionAndAdjustAccountBalanceUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.toRoute<MainDetailTransaction>().id
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    private val _deleteTransactionResult = MutableStateFlow<Result<Int>?>(null)
    val deleteTransactionResult = _deleteTransactionResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val result = deleteTransactionAndAdjustAccountBalanceUseCase(transaction)

            if (result.isSuccess) {
                _deleteTransactionResult.value = result
            } else {
                _deleteTransactionResult.value = Result.failure(NoSuchFieldException())
            }
        }
    }

    fun resetDeleteTransactionResult() {
        _deleteTransactionResult.value = null
    }
}