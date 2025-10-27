package com.android.monu.ui.feature.screen.budgeting.editBudget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.EditBudgetState
import com.android.monu.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monu.domain.usecase.budget.UpdateBudgetUseCase
import com.android.monu.ui.navigation.BudgetDetail
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

    val budgetState = getBudgetByIdUseCase(
        savedStateHandle.toRoute<BudgetDetail.Edit>().budgetId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateBudget(budgetState: EditBudgetState) {
        viewModelScope.launch {
            _updateResult.value = updateBudgetUseCase(budgetState)
            delay(500)
            _updateResult.value = null
        }
    }
}