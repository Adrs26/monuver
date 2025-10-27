package com.android.monu.ui.feature.screen.billing.billDetail

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
import com.android.monu.R
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.BillState
import com.android.monu.ui.feature.components.ConfirmationDialog
import com.android.monu.ui.feature.screen.billing.billDetail.components.BillDetailAppBar
import com.android.monu.ui.feature.screen.billing.billDetail.components.BillDetailContent
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast

@Composable
fun BillDetailScreen(
    billState: BillState,
    billDetailActions: BillDetailActions,
    editResult: DatabaseResultState?,
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(editResult) {
        editResult?.showToast(context)
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

interface BillDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditBill(billId: Long)
    fun onRemoveBill(billId: Long)
    fun onNavigateToPayBill(billId: Long)
    fun onCancelBillPayment(billId: Long)
}