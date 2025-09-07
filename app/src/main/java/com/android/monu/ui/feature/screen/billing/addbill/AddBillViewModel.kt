package com.android.monu.ui.feature.screen.billing.addbill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.bill.CreateBillUseCase
import com.android.monu.ui.feature.screen.billing.addbill.components.AddBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBillViewModel(
    private val createBillUseCase: CreateBillUseCase
) : ViewModel() {

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun createNewBill(billState: AddBillContentState) {
        viewModelScope.launch {
            _createResult.value = createBillUseCase(billState)
            delay(500)
            _createResult.value = null
        }
    }
}