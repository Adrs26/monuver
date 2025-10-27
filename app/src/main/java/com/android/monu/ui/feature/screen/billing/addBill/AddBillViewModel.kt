package com.android.monu.ui.feature.screen.billing.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AddBillState
import com.android.monu.domain.usecase.bill.CreateBillUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBillViewModel(
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