package com.android.monu.ui.feature.screen.billing.billDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.bill.DeleteBillUseCase
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.ui.navigation.Billing
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BillDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    private val deleteBillUseCase: DeleteBillUseCase
) : ViewModel() {

    val bill = getBillByIdUseCase(
        savedStateHandle.toRoute<Billing.Detail>().billId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun deleteBill(billId: Long) {
        viewModelScope.launch {
            deleteBillUseCase(billId)
        }
    }
}