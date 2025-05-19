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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditTransactionViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction.asStateFlow()

    private val _updateResult = MutableStateFlow<Result<Int>?>(null)
    val updateResult = _updateResult.asStateFlow()

    private val _deleteResult = MutableStateFlow<Result<Int>?>(null)
    val deleteResult = _deleteResult.asStateFlow()

    init {
        val id = savedStateHandle.get<Long>("transactionId") ?: 0
        getTransactionById(id)
    }

    fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase.invoke(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun updateTransaction(
        id: Long,
        title: String,
        type: Int,
        category: Int,
        date: String,
        amount: Long,
        budgetingId: Long? = null,
        budgetingTitle: String? = null
    ) {
        viewModelScope.launch {
            if (title.isEmpty() || date.isEmpty() || category == 0 || amount == 0L) {
                _updateResult.value = Result.failure(IllegalArgumentException())
            } else {
                val (month, year) = DateHelper.getMonthAndYear(date)
                val result = updateTransactionUseCase.invoke(
                    Transaction(
                        id = id,
                        title = title,
                        type = type,
                        category = category,
                        date = date,
                        month = month,
                        year = year,
                        amount = amount,
                        budgetingId = budgetingId,
                        budgetingTitle = budgetingTitle
                    )
                )
                _updateResult.value = result
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            val result = deleteTransactionByIdUseCase.invoke(id)
            _deleteResult.value = result
        }
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}