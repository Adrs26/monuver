package com.android.monu.ui.feature.screen.bill.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.bill.Bill

@Composable
fun DueBillScreen(
    bills: List<Bill>
) {
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
                status = 2,
                nowPaidPeriod = bills[index].nowPaidPeriod
            )

            BillListItem(
                billState = billState,
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}