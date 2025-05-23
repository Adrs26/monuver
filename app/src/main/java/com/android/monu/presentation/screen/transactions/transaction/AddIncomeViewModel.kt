package com.android.monu.presentation.screen.transactions.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.usecase.InsertTransactionUseCase
import com.android.monu.util.DateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddIncomeViewModel(
    private val insertTransactionUseCase: InsertTransactionUseCase
) : ViewModel() {

    private val _insertResult = MutableStateFlow<Result<Long>?>(null)
    val insertResult = _insertResult.asStateFlow()

    fun insertTransaction(addIncomeTransactionData: AddIncomeTransactionData) {
        viewModelScope.launch {
            if (
                addIncomeTransactionData.title.isEmpty() ||
                addIncomeTransactionData.date.isEmpty() ||
                addIncomeTransactionData.category == 0 ||
                addIncomeTransactionData.amount == 0L
            ) {
                _insertResult.value = Result.failure(IllegalArgumentException())
            } else {
                val (month, year) = DateHelper.getMonthAndYear(addIncomeTransactionData.date)
                val result = insertTransactionUseCase.invoke(
                    Transaction(
                        id = 0,
                        title = addIncomeTransactionData.title,
                        type = addIncomeTransactionData.type,
                        category = addIncomeTransactionData.category,
                        date = addIncomeTransactionData.date,
                        month = month,
                        year = year,
                        timeStamp = System.currentTimeMillis(),
                        amount = addIncomeTransactionData.amount
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