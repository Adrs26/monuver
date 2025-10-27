package com.android.monu.ui.feature.screen.budgeting.addBudget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AddBudgetState
import com.android.monu.domain.usecase.budget.CreateBudgetUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase
) : ViewModel() {

    private val _transactionCategory = MutableStateFlow(0)
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionCategory(category: Int) {
        _transactionCategory.value = category
    }

    fun createNewBudgeting(budgetState: AddBudgetState) {
        viewModelScope.launch {
            _createResult.value = createBudgetUseCase(budgetState)
            delay(500)
            _createResult.value = null
        }
    }
}