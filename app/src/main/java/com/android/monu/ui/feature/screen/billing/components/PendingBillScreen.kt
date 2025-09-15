package com.android.monu.ui.feature.screen.billing.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun PendingBillScreen(
    bills: List<Bill>,
    onNavigateToBillDetail: (Long) -> Unit
) {
    if (bills.isEmpty()) {
        CommonLottieAnimation(lottieAnimation = R.raw.empty)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = bills.size,
                key = { index -> bills[index].id }
            ) { index ->
                val billState = BillListItemState(
                    id = bills[index].id,
                    title = bills[index].title,
                    dueDate = bills[index].dueDate,
                    paidDate = bills[index].paidDate ?: "",
                    amount = bills[index].amount,
                    isRecurring = bills[index].isRecurring,
                    status = 1,
                    nowPaidPeriod = bills[index].nowPaidPeriod
                )

                BillListItem(
                    billState = billState,
                    modifier = Modifier
                        .debouncedClickable { onNavigateToBillDetail(billState.id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}