package com.android.monu.ui.feature.screen.billing.payBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.domain.usecase.finance.PayBillUseCase
import com.android.monu.ui.feature.screen.billing.payBill.components.PayBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.PayBill
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PayBillViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    getAllAccountsUseCase: GetAllAccountsUseCase,
    private val payBillUseCase: PayBillUseCase
) : ViewModel() {

    val bill = getBillByIdUseCase(
        savedStateHandle.toRoute<PayBill.Main>().billId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val accounts = getAllAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionCategory = MutableStateFlow(Pair(0, 0))
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _transactionSource = MutableStateFlow(Pair(0, ""))
    val transactionSource = _transactionSource.asStateFlow()

    private val _payResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val payResult = _payResult.asStateFlow()

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transactionCategory.value = Pair(parentCategory, childCategory)
    }

    fun changeTransactionSource(sourceId: Int, sourceName: String) {
        _transactionSource.value = Pair(sourceId, sourceName)
    }

    fun payBill(bill: Bill, billState: PayBillContentState) {
        viewModelScope.launch {
            _payResult.value = payBillUseCase(bill, billState)
            delay(500)
            _payResult.value = null
        }
    }
}