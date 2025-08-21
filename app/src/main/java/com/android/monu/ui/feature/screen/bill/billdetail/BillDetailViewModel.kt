package com.android.monu.ui.feature.screen.bill.billdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.ui.navigation.BillDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BillDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase
) : ViewModel() {

    val bill = getBillByIdUseCase(
        savedStateHandle.toRoute<BillDetail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}