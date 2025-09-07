package com.android.monu.ui.feature.screen.billing.editbill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.domain.usecase.bill.UpdateBillUseCase
import com.android.monu.ui.feature.screen.billing.editbill.components.EditBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.EditBill
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

    val bill = getBillByIdUseCase(
        savedStateHandle.toRoute<EditBill>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun updateBill(billState: EditBillContentState) {
        viewModelScope.launch {
            _updateResult.value = updateBillUseCase(billState)
            delay(500)
            _updateResult.value = null
        }
    }
}