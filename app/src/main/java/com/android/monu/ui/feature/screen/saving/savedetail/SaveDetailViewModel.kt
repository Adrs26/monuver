package com.android.monu.ui.feature.screen.saving.savedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.finance.DeleteSaveState
import com.android.monu.domain.usecase.finance.DeleteSaveUseCase
import com.android.monu.domain.usecase.save.GetSaveByIdUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsBySaveIdUseCase
import com.android.monu.ui.navigation.Saving
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SaveDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getSaveByIdUseCase: GetSaveByIdUseCase,
    getAllTransactionsBySaveIdUseCase: GetAllTransactionsBySaveIdUseCase,
    private val deleteSaveUseCase: DeleteSaveUseCase
) : ViewModel() {

    val save = getSaveByIdUseCase(
        savedStateHandle.toRoute<Saving.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions = getAllTransactionsBySaveIdUseCase(
        savedStateHandle.toRoute<Saving.Detail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _deleteProgress = MutableStateFlow<DeleteSaveState>(DeleteSaveState.Loading)
    val deleteProgress = _deleteProgress.asStateFlow()

    fun deleteSave(saveId: Long) {
        viewModelScope.launch {
            deleteSaveUseCase(saveId).collect {
                _deleteProgress.value = it
            }
        }
    }
}