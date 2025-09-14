package com.android.monu.ui.feature.screen.saving.savingDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.usecase.finance.CompleteSavingUseCase
import com.android.monu.domain.usecase.finance.DeleteSavingState
import com.android.monu.domain.usecase.finance.DeleteSavingUseCase
import com.android.monu.domain.usecase.saving.GetSavingByIdUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsBySavingIdUseCase
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.navigation.Saving as SavingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavingDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getSavingByIdUseCase: GetSavingByIdUseCase,
    getAllTransactionsBySavingIdUseCase: GetAllTransactionsBySavingIdUseCase,
    private val deleteSavingUseCase: DeleteSavingUseCase,
    private val completeSavingUseCase: CompleteSavingUseCase
) : ViewModel() {

    val saving = getSavingByIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions = getAllTransactionsBySavingIdUseCase(
        savedStateHandle.toRoute<SavingRoute.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _deleteProgress = MutableStateFlow<DeleteSavingState>(DeleteSavingState.Loading)
    val deleteProgress = _deleteProgress.asStateFlow()

    private val _completeResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val completeResult = _completeResult.asStateFlow()

    fun deleteSaving(savingId: Long) {
        viewModelScope.launch {
            deleteSavingUseCase(savingId).collect { result ->
                _deleteProgress.value = result
            }
        }
    }

    fun completeSaving(saving: Saving) {
        viewModelScope.launch {
            _completeResult.value = completeSavingUseCase(saving.id, saving.title, saving.currentAmount)
        }
    }
}