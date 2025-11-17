package com.android.monuver.feature.billing.presentation.payBill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.android.monuver.core.presentation.navigation.PayBill
import com.android.monuver.feature.billing.domain.model.PayBillState
import com.android.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.android.monuver.feature.billing.domain.usecase.ProcessBillPaymentUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class PayBillViewModel(
    private val processBillPaymentUseCase: ProcessBillPaymentUseCase,
    savedStateHandle: SavedStateHandle,
    getBillByIdUseCase: GetBillByIdUseCase,
    getActiveAccountsUseCase: GetActiveAccountsUseCase
) : ViewModel() {

    val billState = getBillByIdUseCase(
        savedStateHandle.toRoute<PayBill.Main>().billId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val accounts = getActiveAccountsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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