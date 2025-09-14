package com.android.monu.ui.feature.screen.saving.editSaving

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.saving.GetSavingByIdUseCase
import com.android.monu.domain.usecase.finance.UpdateSavingUseCase
import com.android.monu.ui.feature.screen.saving.editSaving.components.EditSavingContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
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

    val saving = getSavingByIdUseCase(
        savedStateHandle.toRoute<Saving.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateSaving(savingState: EditSavingContentState) {
        viewModelScope.launch {
            _updateResult.value = updateSavingUseCase(savingState)
            delay(500)
            _updateResult.value = null
        }
    }
}