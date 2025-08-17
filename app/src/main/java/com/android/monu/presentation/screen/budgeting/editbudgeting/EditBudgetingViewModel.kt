package com.android.monu.presentation.screen.budgeting.editbudgeting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.budgeting.GetBudgetingByIdUseCase
import com.android.monu.domain.usecase.budgeting.UpdateBudgetingUseCase
import com.android.monu.presentation.screen.budgeting.editbudgeting.components.EditBudgetingContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.EditBudgeting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditBudgetingViewModel(
    savedStateHandle: SavedStateHandle,
    getBudgetingByIdUseCase: GetBudgetingByIdUseCase,
    private val updateBudgetingUseCase: UpdateBudgetingUseCase
) : ViewModel() {

    val budgeting = getBudgetingByIdUseCase(
        savedStateHandle.toRoute<EditBudgeting>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateBudgeting(budgetingState: EditBudgetingContentState) {
        viewModelScope.launch {
            val result = updateBudgetingUseCase(budgetingState)
            _updateResult.value = result
            delay(500)
            _updateResult.value = null
        }
    }
}