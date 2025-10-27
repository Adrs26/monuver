package com.android.monu.ui.feature.screen.billing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.android.monu.domain.model.BillState
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.billing.components.BillAppBar
import com.android.monu.ui.feature.screen.billing.components.BillTabRowWithPager

@Composable
fun BillingScreen(
    billUiState: BillUiState,
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
            billUiState = billUiState,
            onNavigateToBillDetail = billActions::onNavigateToBillDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

data class BillUiState(
    val pendingBills: List<BillState>,
    val dueBills: List<BillState>,
    val paidBills: LazyPagingItems<BillState>
)

interface BillActions {
    fun onNavigateBack()
    fun onNavigateToAddBill()
    fun onNavigateToBillDetail(billId: Long)
}