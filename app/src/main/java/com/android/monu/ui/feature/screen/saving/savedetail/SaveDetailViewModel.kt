package com.android.monu.ui.feature.screen.saving.savedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.save.GetSaveByIdUseCase
import com.android.monu.ui.navigation.SaveDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SaveDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getSaveByIdUseCase: GetSaveByIdUseCase
) : ViewModel() {

    val save = getSaveByIdUseCase(
        savedStateHandle.toRoute<SaveDetail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}