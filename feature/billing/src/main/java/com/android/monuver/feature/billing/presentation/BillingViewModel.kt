package com.android.monuver.feature.billing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.android.monuver.core.data.datastore.UserPreferences
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.feature.billing.domain.model.BillListItemState
import com.android.monuver.feature.billing.domain.usecase.GetDueBillsUseCase
import com.android.monuver.feature.billing.domain.usecase.GetPaidBillsUseCase
import com.android.monuver.feature.billing.domain.usecase.GetPendingBillsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BillingViewModel(
    val preference: UserPreferences,
    getPendingBillsUseCase: GetPendingBillsUseCase,
    getDueBillsUseCase: GetDueBillsUseCase,
    getPaidBillsUseCase: GetPaidBillsUseCase,
    customDispatcher: CustomDispatcher
) : ViewModel() {

    val pendingBills = getPendingBillsUseCase()
        .map { bills ->
            bills.map { billState ->
                BillListItemState(
                    id = billState.id,
                    title = billState.title,
                    dueDate = billState.dueDate,
                    paidDate = billState.paidDate ?: "",
                    amount = billState.amount,
                    isRecurring = billState.isRecurring,
                    status = 1,
                    nowPaidPeriod = billState.nowPaidPeriod
                )
            }
        }
        .flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dueBills = getDueBillsUseCase()
        .map { bills ->
            bills.map { billState ->
                BillListItemState(
                    id = billState.id,
                    title = billState.title,
                    dueDate = billState.dueDate,
                    paidDate = billState.paidDate ?: "",
                    amount = billState.amount,
                    isRecurring = billState.isRecurring,
                    status = 2,
                    nowPaidPeriod = billState.nowPaidPeriod
                )
            }
        }
        .flowOn(customDispatcher.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val paidBills = getPaidBillsUseCase(viewModelScope)
        .map { bills ->
            bills.map { billState ->
                BillListItemState(
                    id = billState.id,
                    title = billState.title,
                    dueDate = billState.dueDate,
                    paidDate = billState.paidDate ?: "",
                    amount = billState.amount,
                    isRecurring = billState.isRecurring,
                    status = 3,
                    nowPaidPeriod = billState.nowPaidPeriod
                )
            }
        }
        .flowOn(customDispatcher.default)

    val reminderDaysBeforeDue = preference.reminderDaysBeforeDue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1
        )

    val isReminderBeforeDueDayEnabled = preference.isReminderBeforeDueDayEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isReminderForDueBillEnabled = preference.isReminderForDueBillEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setReminderSettings(
        reminderDaysBeforeDue: Int,
        isReminderBeforeDueDayEnabled: Boolean,
        isReminderForDueBillEnabled: Boolean
    ) {
        viewModelScope.launch {
            preference.setReminderSettings(
                reminderDaysBeforeDue = reminderDaysBeforeDue,
                isReminderBeforeDueDayEnabled = isReminderBeforeDueDayEnabled,
                isReminderForDueBillEnabled = isReminderForDueBillEnabled
            )
        }
    }
}