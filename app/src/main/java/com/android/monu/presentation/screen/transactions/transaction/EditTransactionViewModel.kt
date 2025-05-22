package com.android.monu.presentation.screen.transactions.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.usecase.DeleteTransactionByIdUseCase
import com.android.monu.domain.usecase.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.UpdateTransactionUseCase
import com.android.monu.util.DateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.get<Long>("transactionId") ?: 0
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<Result<Int>?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase.invoke(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun updateTransaction(updateTransactionData: UpdateTransactionData) {
        viewModelScope.launch {
            if (
                updateTransactionData.title.isEmpty() ||
                updateTransactionData.date.isEmpty() ||
                updateTransactionData.category == 0 ||
                updateTransactionData.amount == 0L
            ) {
                _updateResult.value = Result.failure(IllegalArgumentException())
            } else {
                val (month, year) = DateHelper.getMonthAndYear(updateTransactionData.date)
                val result = updateTransactionUseCase.invoke(
                    Transaction(
                        id = updateTransactionData.id,
                        title = updateTransactionData.title,
                        type = updateTransactionData.type,
                        category = updateTransactionData.category,
                        date = updateTransactionData.date,
                        month = month,
                        year = year,
                        amount = updateTransactionData.amount,
                        budgetingId = updateTransactionData.budgetingId,
                        budgetingTitle = updateTransactionData.budgetingTitle
                    )
                )
                _updateResult.value = result
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch { deleteTransactionByIdUseCase.invoke(id) }
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }
}