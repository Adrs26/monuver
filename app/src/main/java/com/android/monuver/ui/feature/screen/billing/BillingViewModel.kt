package com.android.monuver.ui.feature.screen.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.android.monuver.data.datastore.UserPreferences
import com.android.monuver.domain.common.CoroutineDispatchers
import com.android.monuver.domain.usecase.bill.GetDueBillsUseCase
import com.android.monuver.domain.usecase.bill.GetPaidBillsUseCase
import com.android.monuver.domain.usecase.bill.GetPendingBillsUseCase
import com.android.monuver.ui.feature.screen.billing.components.BillListItemState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.map

class BillingViewModel(
    val preference: UserPreferences,
    getPendingBillsUseCase: GetPendingBillsUseCase,
    getDueBillsUseCase: GetDueBillsUseCase,
    getPaidBillsUseCase: GetPaidBillsUseCase,
    coroutineDispatchers: CoroutineDispatchers
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
        .flowOn(coroutineDispatchers.default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
        .flowOn(coroutineDispatchers.default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
        .flowOn(coroutineDispatchers.default)

    val reminderDaysBeforeDue = preference.reminderDaysBeforeDue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val isReminderBeforeDueDayEnabled = preference.isReminderBeforeDueDayEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isReminderForDueBillEnabled = preference.isReminderForDueBillEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

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