package com.android.monu.presentation.screen.budgeting.editbudget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monu.domain.usecase.budget.UpdateBudgetUseCase
import com.android.monu.presentation.screen.budgeting.editbudget.components.EditBudgetContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.EditBudget
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditBudgetViewModel(
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase
) : ViewModel() {

    val budget = getBudgetByIdUseCase(
        savedStateHandle.toRoute<EditBudget>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateBudget(budgetState: EditBudgetContentState) {
        viewModelScope.launch {
            val result = updateBudgetUseCase(budgetState)
            _updateResult.value = result
            delay(500)
            _updateResult.value = null
        }
    }
}