package com.android.monu.presentation.screen.transaction.edittransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.UpdateTransferTransactionUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContentState
import com.android.monu.ui.navigation.MainDetailTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditTransferViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransferTransactionUseCase: UpdateTransferTransactionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction
        .onStart {
            val id = savedStateHandle.toRoute<MainDetailTransaction>().id
            getTransactionById(id)
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    private val _updateResult = MutableStateFlow<Result<Int>?>(null)
    val updateResult = _updateResult.asStateFlow()

    private fun getTransactionById(id: Long) {
        viewModelScope.launch {
            getTransactionByIdUseCase(id).collect { transaction ->
                _transaction.value = transaction
            }
        }
    }

    fun updateTransaction(transferState: EditTransferContentState) {
        viewModelScope.launch {
            if (transferState.date.isEmpty() || transferState.amount == 0L) {
                _updateResult.value = Result.failure(
                    IllegalArgumentException("Harap lengkapi semua kolom yang tersedia")
                )
                delay(500)
                _updateResult.value = null
            } else {
                val result = updateTransferTransactionUseCase(transferState)
                _updateResult.value = result
                delay(500)
                _updateResult.value = null
            }
        }
    }
}