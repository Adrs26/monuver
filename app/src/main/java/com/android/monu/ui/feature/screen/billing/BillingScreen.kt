package com.android.monu.ui.feature.screen.billing

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.BillState
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.billing.components.BillAppBar
import com.android.monu.ui.feature.screen.billing.components.BillReminderDialog
import com.android.monu.ui.feature.screen.billing.components.BillTabRowWithPager
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.worker.startReminderWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BillingScreen(
    billUiState: BillUiState,
    billActions: BillActions
) {
    var showReminderDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    startReminderWorker(context)
                    showReminderDialog = true
                } else {
                    context.getString(
                        R.string.notification_permissions_required_to_use_bill_reminder_feature
                    ).showMessageWithToast(context)
                }
            }
        )
    } else { null }

    Scaffold(
        topBar = {
            BillAppBar(
                onNavigateBack = billActions::onNavigateBack,
                onReminderClick = {
                    if (notificationsPermissionState?.status?.isGranted == true) {
                        showReminderDialog = true
                    } else {
                        notificationsPermissionState?.launchPermissionRequest()
                    }
                }
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                billActions.onNavigateToAddBill()
            }
        }
    ) { innerPadding ->
        BillTabRowWithPager(
            billUiState = billUiState,
            onNavigateToBillDetail = billActions::onNavigateToBillDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

    if (showReminderDialog) {
        BillReminderDialog(
            reminderDaysBeforeDue = billUiState.reminderDaysBeforeDue,
            isReminderBeforeDueDayEnabled = billUiState.isReminderBeforeDueDayEnabled,
            isReminderForDueBillEnabled = billUiState.isReminderForDueBillEnabled,
            onDismissRequest = { showReminderDialog = false },
            onSettingsApply = billActions::onSettingsApply
        )
    }
}

data class BillUiState(
    val pendingBills: List<BillState>,
    val dueBills: List<BillState>,
    val paidBills: LazyPagingItems<BillState>,
    val reminderDaysBeforeDue: Int,
    val isReminderBeforeDueDayEnabled: Boolean,
    val isReminderForDueBillEnabled: Boolean,
)

interface BillActions {
    fun onNavigateBack()
    fun onNavigateToAddBill()
    fun onNavigateToBillDetail(billId: Long)
    fun onSettingsApply(
        reminderDaysBeforeDue: Int,
        isReminderBeforeDueDayEnabled: Boolean,
        isReminderForDueBillEnabled: Boolean,
    )
}