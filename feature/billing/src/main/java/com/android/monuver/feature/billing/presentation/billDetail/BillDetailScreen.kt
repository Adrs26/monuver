package com.android.monuver.feature.billing.presentation.billDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.billing.R
import com.android.monuver.feature.billing.presentation.billDetail.components.BillDetailAppBar
import com.android.monuver.feature.billing.presentation.billDetail.components.BillDetailContent

@Composable
internal fun BillDetailScreen(
    billState: BillState,
    result: DatabaseResultState?,
    billDetailActions: BillDetailActions
) {
    val context = LocalContext.current

    var showRemoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        result?.showToast(context)
    }

    Scaffold(
        topBar = {
            BillDetailAppBar(
                isPaid = billState.isPaid,
                onNavigateBack = billDetailActions::onNavigateBack,
                onNavigateToEditBill = { billDetailActions.onNavigateToEditBill(billState.id) },
                onRemoveBill = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        BillDetailContent(
            billState = billState,
            onNavigateToPayBill = billDetailActions::onNavigateToPayBill,
            onCancelBillPayment = billDetailActions::onCancelBillPayment,
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 24.dp),
        )
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            text = context.getString(R.string.delete_this_bill),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                billDetailActions.onRemoveBill(billState.id)
                context.getString(R.string.bill_successfully_deleted).showMessageWithToast(context)
                billDetailActions.onNavigateBack()
            }
        )
    }
}

internal interface BillDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditBill(billId: Long)
    fun onRemoveBill(billId: Long)
    fun onNavigateToPayBill(billId: Long)
    fun onCancelBillPayment(billId: Long)
}