package com.android.monu.presentation.screen.budgeting.addbudgeting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddBudgetingViewModel : ViewModel() {

    private val _transactionCategory = MutableStateFlow<Int>(0)
    val transactionCategory = _transactionCategory.asStateFlow()

    fun changeTransactionCategory(category: Int) {
        _transactionCategory.value = category
    }
}