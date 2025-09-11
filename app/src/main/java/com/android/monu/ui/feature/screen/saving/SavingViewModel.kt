package com.android.monu.ui.feature.screen.saving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.save.GetAllSavesUseCase
import com.android.monu.domain.usecase.save.GetTotalSaveCurrentAmountUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SavingViewModel(
    getTotalSaveCurrentAmountUseCase: GetTotalSaveCurrentAmountUseCase,
    getAllSavesUseCase: GetAllSavesUseCase
) : ViewModel() {

    val totalCurrentAmount = getTotalSaveCurrentAmountUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val saves = getAllSavesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}