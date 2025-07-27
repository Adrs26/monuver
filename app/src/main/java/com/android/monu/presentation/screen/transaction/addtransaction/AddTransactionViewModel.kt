package com.android.monu.presentation.screen.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
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

    private val _createResult = MutableStateFlow<Result<Long>?>(null)
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
            if (
                addTransactionState.title.isEmpty() ||
                addTransactionState.date.isEmpty() ||
                addTransactionState.childCategory == 0 ||
                addTransactionState.amount == 0L ||
                addTransactionState.sourceId == 0
            ) {
                _createResult.value = Result.failure(
                    IllegalArgumentException("Semua field harus diisi ya")
                )
                delay(500)
                _createResult.value = null
            } else {
                val result = when (addTransactionState.type) {
                    TransactionType.INCOME -> createIncomeTransactionUseCase(addTransactionState)
                    TransactionType.EXPENSE -> createExpenseTransactionUseCase(addTransactionState)
                    else -> Result.failure(IllegalArgumentException("Tipe transaksi tidak valid"))
                }
                _createResult.value = result
                delay(500)
                _createResult.value = null
            }
        }
    }
}