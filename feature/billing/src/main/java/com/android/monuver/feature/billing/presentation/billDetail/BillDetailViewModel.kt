package com.android.monuver.feature.billing.presentation.billDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.navigation.Billing
import com.android.monuver.feature.billing.domain.usecase.CancelBillPaymentUseCase
import com.android.monuver.feature.billing.domain.usecase.DeleteBillUseCase
import com.android.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BillDetailViewModel(
    private val deleteBillUseCase: DeleteBillUseCase,
    private val cancelBillPaymentUseCase: CancelBillPaymentUseCase,
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<Billing.Detail>().billId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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