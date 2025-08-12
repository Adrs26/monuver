package com.android.monu.presentation.screen.budgeting.budgetingdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.monu.domain.usecase.budgeting.DeleteBudgetingUseCase
import com.android.monu.domain.usecase.budgeting.GetBudgetingByIdUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import com.android.monu.ui.navigation.MainBudgetingDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetingDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getBudgetingByIdUseCase: GetBudgetingByIdUseCase,
    getTransactionsByCategoryAndDateRangeUseCase: GetTransactionsByCategoryAndDateRangeUseCase,
    private val deleteBudgetingUseCase: DeleteBudgetingUseCase
) : ViewModel() {

    val budgeting = getBudgetingByIdUseCase(
        savedStateHandle.toRoute<MainBudgetingDetail>().id
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = budgeting
        .filterNotNull()
        .flatMapLatest { budgeting ->
            getTransactionsByCategoryAndDateRangeUseCase(
                budgeting.category,
                budgeting.startDate,
                budgeting.endDate
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBudgeting(budgetingId: Long) {
        viewModelScope.launch {
            deleteBudgetingUseCase(budgetingId)
        }
    }
}