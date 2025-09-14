package com.android.monu.ui.feature.screen.saving.inactiveSaving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.saving.GetAllInactiveSavingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class InactiveSavingViewModel(
    getAllInactiveSavingsUseCase: GetAllInactiveSavingsUseCase
): ViewModel() {

    val savings = getAllInactiveSavingsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}