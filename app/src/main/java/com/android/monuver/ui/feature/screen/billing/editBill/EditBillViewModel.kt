package com.android.monuver.ui.feature.screen.billing.editBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.EditBillState
import com.android.monuver.domain.usecase.bill.GetBillByIdUseCase
import com.android.monuver.domain.usecase.bill.UpdateBillUseCase
import com.android.monuver.ui.navigation.Billing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditBillViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    private val updateBillUseCase: UpdateBillUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<Billing.Edit>().billId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultState?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateBill(billState: EditBillState) {
        viewModelScope.launch {
            _updateResult.value = updateBillUseCase(billState)
            delay(500)
            _updateResult.value = null
        }
    }
}