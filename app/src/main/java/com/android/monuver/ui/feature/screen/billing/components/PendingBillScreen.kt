package com.android.monuver.ui.feature.screen.billing.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monuver.R
import com.android.monuver.ui.feature.components.CommonLottieAnimation
import com.android.monuver.ui.feature.utils.debouncedClickable

@Composable
fun PendingBillScreen(
    bills: List<BillListItemState>,
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
                BillListItem(
                    billState = bills[index],
                    modifier = Modifier
                        .debouncedClickable { onNavigateToBillDetail(bills[index].id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}