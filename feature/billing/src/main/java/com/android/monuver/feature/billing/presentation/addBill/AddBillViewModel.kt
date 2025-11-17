package com.android.monuver.feature.billing.presentation.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.feature.billing.domain.model.AddBillState
import com.android.monuver.feature.billing.domain.usecase.CreateBillUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AddBillViewModel(
    private val createBillUseCase: CreateBillUseCase
) : ViewModel() {

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun createNewBill(billState: AddBillState) {
        viewModelScope.launch {
            _createResult.value = createBillUseCase(billState)
            delay(500)
            _createResult.value = null
        }
    }
}