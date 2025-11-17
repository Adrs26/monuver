package com.android.monuver.feature.billing.presentation.editBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.navigation.Billing
import com.android.monuver.feature.billing.domain.model.EditBillState
import com.android.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.android.monuver.feature.billing.domain.usecase.UpdateBillUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class EditBillViewModel(
    private val updateBillUseCase: UpdateBillUseCase,
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<Billing.Edit>().billId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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