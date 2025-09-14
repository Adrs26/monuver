package com.android.monu.ui.feature.screen.saving.editsave

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.save.GetSaveByIdUseCase
import com.android.monu.domain.usecase.finance.UpdateSaveUseCase
import com.android.monu.ui.feature.screen.saving.editsave.components.EditSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.Saving
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditSaveViewModel(
    savedStateHandle: SavedStateHandle,
    getSaveByIdUseCase: GetSaveByIdUseCase,
    private val updateSaveUseCase: UpdateSaveUseCase
) : ViewModel() {

    val save = getSaveByIdUseCase(
        savedStateHandle.toRoute<Saving.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateSave(saveState: EditSaveContentState) {
        viewModelScope.launch {
            _updateResult.value = updateSaveUseCase(saveState)
            delay(500)
            _updateResult.value = null
        }
    }
}