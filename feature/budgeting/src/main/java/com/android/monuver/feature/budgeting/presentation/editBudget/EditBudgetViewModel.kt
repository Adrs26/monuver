package com.android.monuver.feature.budgeting.presentation.editBudget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.navigation.BudgetDetail
import com.android.monuver.feature.budgeting.domain.model.EditBudgetState
import com.android.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.android.monuver.feature.budgeting.domain.usecase.UpdateBudgetUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class EditBudgetViewModel(
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    savedStateHandle: SavedStateHandle,
    getBudgetByIdUseCase: GetBudgetByIdUseCase
) : ViewModel() {

    val budgetState = getBudgetByIdUseCase(
        savedStateHandle.toRoute<BudgetDetail.Edit>().budgetId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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