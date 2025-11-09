package com.android.monu.ui.feature.screen.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.UserPreferences
import com.android.monu.domain.usecase.bill.GetDueBillsUseCase
import com.android.monu.domain.usecase.bill.GetPaidBillsUseCase
import com.android.monu.domain.usecase.bill.GetPendingBillsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BillingViewModel(
    getPendingBillsUseCase: GetPendingBillsUseCase,
    getDueBillsUseCase: GetDueBillsUseCase,
    getPaidBillsUseCase: GetPaidBillsUseCase,
    val preference: UserPreferences
) : ViewModel() {

    val pendingBills = getPendingBillsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueBills = getDueBillsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paidBills = getPaidBillsUseCase(viewModelScope)

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