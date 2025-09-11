package com.android.monu.ui.feature.screen.saving.addsave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.save.CreateSaveUseCase
import com.android.monu.ui.feature.screen.saving.addsave.components.AddSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddSaveViewModel(
    private val createSaveUseCase: CreateSaveUseCase
) : ViewModel() {

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun createNewSave(saveState: AddSaveContentState) {
        viewModelScope.launch {
            _createResult.value = createSaveUseCase(saveState)
            delay(500)
            _createResult.value = null
        }
    }
}