package com.android.monuver.ui.feature.screen.saving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.usecase.saving.GetAllActiveSavingsUseCase
import com.android.monuver.domain.usecase.saving.GetTotalSavingCurrentAmountUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SavingViewModel(
    getTotalSavingCurrentAmountUseCase: GetTotalSavingCurrentAmountUseCase,
    getAllActiveSavingsUseCase: GetAllActiveSavingsUseCase
) : ViewModel() {

    val totalCurrentAmount = getTotalSavingCurrentAmountUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val savings = getAllActiveSavingsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}