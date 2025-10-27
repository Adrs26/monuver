package com.android.monu.ui.feature.screen.billing.billDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.usecase.bill.DeleteBillUseCase
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.domain.usecase.finance.CancelBillPaymentUseCase
import com.android.monu.ui.navigation.Billing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BillDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    private val deleteBillUseCase: DeleteBillUseCase,
    private val cancelBillPaymentUseCase: CancelBillPaymentUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<Billing.Detail>().billId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun deleteBill(billId: Long) {
        viewModelScope.launch {
            deleteBillUseCase(billId)
        }
    }

    fun cancelBillPayment(billId: Long) {
        viewModelScope.launch {
            _updateResult.value = cancelBillPaymentUseCase(billId)
            delay(500)
            _updateResult.value = null
        }
    }
}