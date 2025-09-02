package com.android.monu.ui.feature.screen.bill

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.bill.components.BillAppBar
import com.android.monu.ui.feature.screen.bill.components.BillTabRowWithPager

@Composable
fun BillingScreen(
    billState: BillState,
    billActions: BillActions
) {
    Scaffold(
        topBar = {
            BillAppBar(
                onNavigateBack = billActions::onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                billActions.onNavigateToAddBill()
            }
        }
    ) { innerPadding ->
        BillTabRowWithPager(
            billState = billState,
            onNavigateToBillDetail = billActions::onNavigateToBillDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

data class BillState(
    val pendingBills: List<Bill>,
    val dueBills: List<Bill>,
    val paidBills: LazyPagingItems<Bill>
)

interface BillActions {
    fun onNavigateBack()
    fun onNavigateToAddBill()
    fun onNavigateToBillDetail(billId: Long)
}