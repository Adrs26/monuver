package com.android.monu.ui.feature.screen.saving.addSaving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AddSavingState
import com.android.monu.domain.usecase.saving.CreateSavingUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddSavingViewModel(
    private val createSavingUseCase: CreateSavingUseCase
) : ViewModel() {

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun createNewSaving(savingState: AddSavingState) {
        viewModelScope.launch {
            _createResult.value = createSavingUseCase(savingState)
            delay(500)
            _createResult.value = null
        }
    }
}