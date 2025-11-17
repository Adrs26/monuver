package com.android.monuver.feature.saving.presentation.savingDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.mapper.toListItemState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.feature.saving.domain.common.DeleteSavingStatusState
import com.android.monuver.feature.saving.domain.usecase.CompleteSavingUseCase
import com.android.monuver.feature.saving.domain.usecase.DeleteSavingUseCase
import com.android.monuver.feature.saving.domain.usecase.GetAllTransactionsBySavingIdUseCase
import com.android.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.android.monuver.core.presentation.navigation.Saving as SavingRoute

internal class SavingDetailViewModel(
    private val deleteSavingUseCase: DeleteSavingUseCase,
    private val completeSavingUseCase: CompleteSavingUseCase,
    savedStateHandle: SavedStateHandle,
    getSavingByIdUseCase: GetSavingByIdUseCase,
    getAllTransactionsBySavingIdUseCase: GetAllTransactionsBySavingIdUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val saving = getSavingByIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val transactions = getAllTransactionsBySavingIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).map { transactions ->
        transactions.map { transactionState ->
            transactionState.toListItemState()
        }
    }.flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
            _completeResult.value = completeSavingUseCase(
                savingId = savingState.id,
                savingName = savingState.title,
                savingAmount = savingState.currentAmount
            )
        }
    }
}