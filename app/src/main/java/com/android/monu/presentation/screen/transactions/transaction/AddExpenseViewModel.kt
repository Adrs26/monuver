package com.android.monu.presentation.screen.transactions.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.usecase.InsertTransactionUseCase
import com.android.monu.util.DateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    private val insertTransactionUseCase: InsertTransactionUseCase
) : ViewModel() {

    private val _insertResult = MutableStateFlow<Result<Long>?>(null)
    val insertResult = _insertResult.asStateFlow()

    fun insertTransaction(addExpenseTransactionData: AddExpenseTransactionData) {
        viewModelScope.launch {
            if (
                addExpenseTransactionData.title.isEmpty() ||
                addExpenseTransactionData.date.isEmpty() ||
                addExpenseTransactionData.category == 0 ||
                addExpenseTransactionData.amount == 0L
            ) {
                _insertResult.value = Result.failure(IllegalArgumentException())
            } else {
                val (month, year) = DateHelper.getMonthAndYear(addExpenseTransactionData.date)
                val result = insertTransactionUseCase.invoke(
                    Transaction(
                        id = 0,
                        title = addExpenseTransactionData.title,
                        type = addExpenseTransactionData.type,
                        category = addExpenseTransactionData.category,
                        date = addExpenseTransactionData.date,
                        month = month,
                        year = year,
                        amount = addExpenseTransactionData.amount,
                        budgetingId = addExpenseTransactionData.budgetingId,
                        budgetingTitle = addExpenseTransactionData.budgetingTitle
                    )
                )
                _insertResult.value = result
            }
        }
    }

    fun resetInsertResult() {
        _insertResult.value = null
    }
}