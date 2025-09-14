package com.android.monu.ui.feature.screen.billing.billDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.screen.billing.billDetail.components.BillDetailAppBar
import com.android.monu.ui.feature.screen.billing.billDetail.components.BillDetailContent
import com.android.monu.ui.feature.screen.billing.billDetail.components.RemoveBillDialog
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun BillDetailScreen(
    bill: Bill,
    billDetailActions: BillDetailActions
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BillDetailAppBar(
                isPaid = bill.isPaid,
                onNavigateBack = billDetailActions::onNavigateBack,
                onNavigateToEditBill = { billDetailActions.onNavigateToEditBill(bill.id) },
                onRemoveBill = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        BillDetailContent(
            bill = bill,
            onNavigateToPayBill = billDetailActions::onNavigateToPayBill,
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 24.dp),
        )
    }

    if (showRemoveDialog) {
        RemoveBillDialog(
            onDismissRequest = { showRemoveDialog = false },
            onRemoveBill = {
                showRemoveDialog = false
                billDetailActions.onRemoveBill(bill.id)
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
}