package com.android.monu.ui.feature.screen.bill.billdetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.screen.bill.billdetail.components.BillDetailAppBar
import com.android.monu.ui.feature.screen.bill.billdetail.components.BillDetailContent

@Composable
fun BillDetailScreen(
    bill: Bill,
    onNavigateBack: () -> Unit,
    onNavigateToPayBill: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            BillDetailAppBar(
                onNavigateBack = onNavigateBack,
                onNavigateToEditBill = {},
                onRemoveBill = {}
            )
        }
    ) { innerPadding ->
        BillDetailContent(
            bill = bill,
            onNavigateToPayBill = onNavigateToPayBill,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp),
        )
    }
}