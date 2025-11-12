package com.android.monuver.ui.feature.screen.billing.payBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.model.PayBillState
import com.android.monuver.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monuver.domain.usecase.bill.GetBillByIdUseCase
import com.android.monuver.domain.usecase.finance.ProcessBillPaymentUseCase
import com.android.monuver.ui.navigation.PayBill
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PayBillViewModel(
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
    private val processBillPaymentUseCase: ProcessBillPaymentUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<PayBill.Main>().billId
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val accounts = getActiveAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionCategory = MutableStateFlow(Pair(0, 0))
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _transactionSource = MutableStateFlow(Pair(0, ""))
    val transactionSource = _transactionSource.asStateFlow()

    private val _payResult = MutableStateFlow<DatabaseResultState?>(null)
    val payResult = _payResult.asStateFlow()

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transactionCategory.value = Pair(parentCategory, childCategory)
    }

    fun changeTransactionSource(sourceId: Int, sourceName: String) {
        _transactionSource.value = Pair(sourceId, sourceName)
    }

    fun payBill(bill: BillState, billState: PayBillState) {
        viewModelScope.launch {
            _payResult.value = processBillPaymentUseCase(bill, billState)
            delay(500)
            _payResult.value = null
        }
    }
}