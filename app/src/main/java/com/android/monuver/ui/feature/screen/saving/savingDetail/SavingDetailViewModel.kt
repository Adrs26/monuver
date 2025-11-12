package com.android.monuver.ui.feature.screen.saving.savingDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.domain.common.CoroutineDispatchers
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.common.DeleteSavingStatusState
import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.usecase.finance.CompleteSavingUseCase
import com.android.monuver.domain.usecase.finance.DeleteSavingUseCase
import com.android.monuver.domain.usecase.saving.GetSavingByIdUseCase
import com.android.monuver.domain.usecase.transaction.GetAllTransactionsBySavingIdUseCase
import com.android.monuver.ui.feature.components.TransactionListItemState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.android.monuver.ui.navigation.Saving as SavingRoute

class SavingDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getSavingByIdUseCase: GetSavingByIdUseCase,
    getAllTransactionsBySavingIdUseCase: GetAllTransactionsBySavingIdUseCase,
    private val deleteSavingUseCase: DeleteSavingUseCase,
    private val completeSavingUseCase: CompleteSavingUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    val saving = getSavingByIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions = getAllTransactionsBySavingIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).map { transactions ->
        transactions.map { transactionState ->
            TransactionListItemState(
                id = transactionState.id,
                title = transactionState.title,
                type = transactionState.type,
                parentCategory = transactionState.parentCategory,
                childCategory = transactionState.childCategory,
                date = transactionState.date,
                amount = transactionState.amount,
                sourceName = transactionState.sourceName,
                isLocked = transactionState.isLocked
            )
        }
    }.flowOn(coroutineDispatchers.default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _deleteProgress = MutableStateFlow<DeleteSavingStatusState>(DeleteSavingStatusState.Idle)
    val deleteProgress = _deleteProgress.asStateFlow()

    private val _completeResult = MutableStateFlow<DatabaseResultState?>(null)
    val completeResult = _completeResult.asStateFlow()

    fun deleteSaving(savingId: Long) {
        viewModelScope.launch {
            deleteSavingUseCase(savingId).collect { result ->
                _deleteProgress.value = result
            }
        }
    }

    fun completeSaving(savingState: SavingState) {
        viewModelScope.launch {
            _completeResult.value = completeSavingUseCase(savingState.id, savingState.title, savingState.currentAmount)
        }
    }
}