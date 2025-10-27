package com.android.monu.ui.feature.screen.saving.editSaving

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.EditSavingState
import com.android.monu.domain.usecase.finance.UpdateSavingUseCase
import com.android.monu.domain.usecase.saving.GetSavingByIdUseCase
import com.android.monu.ui.navigation.Saving
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditSavingViewModel(
    savedStateHandle: SavedStateHandle,
    getSavingByIdUseCase: GetSavingByIdUseCase,
    private val updateSavingUseCase: UpdateSavingUseCase
) : ViewModel() {

    val savingState = getSavingByIdUseCase(
        savedStateHandle.toRoute<Saving.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateSaving(savingState: EditSavingState) {
        viewModelScope.launch {
            _updateResult.value = updateSavingUseCase(savingState)
            delay(500)
            _updateResult.value = null
        }
    }
}