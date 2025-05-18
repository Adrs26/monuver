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

    private val _selectedCategory = MutableStateFlow(0)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedBudgetingId = MutableStateFlow<Long?>(null)
    val selectedBudgetingId = _selectedBudgetingId.asStateFlow()

    private val _selectedBudgetingTitle = MutableStateFlow<String?>(null)
    val selectedBudgetingTitle = _selectedBudgetingTitle.asStateFlow()

    fun selectCategory(category: Int) {
        _selectedCategory.value = category
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun insertTransaction(
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
                _insertResult.value = Result.failure(IllegalArgumentException())
            } else {
                val (month, year) = DateHelper.getMonthAndYear(date)
                val result = insertTransactionUseCase.invoke(
                    Transaction(
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
                _insertResult.value = result
            }
        }
    }

    fun resetInsertResult() {
        _insertResult.value = null
    }
}