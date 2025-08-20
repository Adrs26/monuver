package com.android.monu.ui.feature.screen.bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.bill.GetDueBillsUseCase
import com.android.monu.domain.usecase.bill.GetPendingBillsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BillViewModel(
    getPendingBillsUseCase: GetPendingBillsUseCase,
    getDueBillsUseCase: GetDueBillsUseCase
) : ViewModel() {

    val pendingBills = getPendingBillsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueBills = getDueBillsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}