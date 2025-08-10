package com.android.monu.presentation.screen.budgeting.addbudgeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.budgeting.CreateBudgetingUseCase
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBudgetingViewModel(
    private val createBudgetingUseCase: CreateBudgetingUseCase
) : ViewModel() {

    private val _transactionCategory = MutableStateFlow<Int>(0)
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionCategory(category: Int) {
        _transactionCategory.value = category
    }

    fun createNewBudgeting(budgetingState: AddBudgetingContentState) {
        viewModelScope.launch {
            _createResult.value = createBudgetingUseCase(budgetingState)
            delay(500)
            _createResult.value = null
        }
    }
}