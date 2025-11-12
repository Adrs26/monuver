package com.android.monuver.ui.feature.screen.transaction.addTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddTransactionState
import com.android.monuver.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monuver.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monuver.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monuver.utils.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    getActiveAccountsUseCase: GetActiveAccountsUseCase,
    private val createIncomeTransactionUseCase: CreateIncomeTransactionUseCase,
    private val createExpenseTransactionUseCase: CreateExpenseTransactionUseCase
) : ViewModel() {

    val accounts = getActiveAccountsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transactionCategory = MutableStateFlow(Pair(0, 0))
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _transactionSource = MutableStateFlow(Pair(0, ""))
    val transactionSource = _transactionSource.asStateFlow()

    private val _createResult = MutableStateFlow<DatabaseResultState?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transactionCategory.value = Pair(parentCategory, childCategory)
    }

    fun changeTransactionSource(sourceId: Int, sourceName: String) {
        _transactionSource.value = Pair(sourceId, sourceName)
    }

    fun createNewTransaction(addTransactionState: AddTransactionState) {
        viewModelScope.launch {
            val result = when (addTransactionState.type) {
                TransactionType.INCOME -> createIncomeTransactionUseCase(addTransactionState)
                TransactionType.EXPENSE -> createExpenseTransactionUseCase(addTransactionState)
                else -> null
            }
            _createResult.value = result
            delay(500)
            _createResult.value = null
        }
    }
}