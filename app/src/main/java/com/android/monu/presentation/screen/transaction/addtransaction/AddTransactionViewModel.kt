package com.android.monu.presentation.screen.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val createIncomeTransactionUseCase: CreateIncomeTransactionUseCase,
    private val createExpenseTransactionUseCase: CreateExpenseTransactionUseCase
) : ViewModel() {

    private val _transactionCategory = MutableStateFlow<Pair<Int, Int>>(Pair(0, 0))
    val transactionCategory = _transactionCategory.asStateFlow()

    private val _transactionSource = MutableStateFlow<Pair<Int, String>>(Pair(0, ""))
    val transactionSource = _transactionSource.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts
        .onStart {
            getAllAccounts()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _createResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val createResult = _createResult.asStateFlow()

    fun changeTransactionCategory(parentCategory: Int, childCategory: Int) {
        _transactionCategory.value = Pair(parentCategory, childCategory)
    }

    fun changeTransactionSource(sourceId: Int, sourceName: String) {
        _transactionSource.value = Pair(sourceId, sourceName)
    }

    private fun getAllAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase().collect { accounts ->
                _accounts.value = accounts
            }
        }
    }

    fun createNewTransaction(addTransactionState: AddTransactionContentState) {
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