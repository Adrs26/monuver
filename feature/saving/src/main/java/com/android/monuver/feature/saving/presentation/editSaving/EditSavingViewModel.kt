package com.android.monuver.feature.saving.presentation.editSaving

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.navigation.Saving
import com.android.monuver.feature.saving.domain.model.EditSavingState
import com.android.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import com.android.monuver.feature.saving.domain.usecase.UpdateSavingUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class EditSavingViewModel(
    private val updateSavingUseCase: UpdateSavingUseCase,
    savedStateHandle: SavedStateHandle,
    getSavingByIdUseCase: GetSavingByIdUseCase
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