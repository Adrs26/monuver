package com.android.monu.ui.feature.screen.bill.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.theme.Red600

@Composable
fun BillListItem(
    billState: BillListItemState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = billState.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = getBillDateInformationText(
                    status = billState.status,
                    dueDate = billState.dueDate,
                    paidDate = billState.paidDate
                ),
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = getBillDateInformationColor(billState.status)
                )
            )
            if (billState.isRecurring) {
                Text(
                    text = "Pembayaran ke-${billState.nowPaidPeriod}",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
        Text(
            text = NumberFormatHelper.formatToRupiah(billState.amount),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

private fun getBillDateInformationText(status: Int, dueDate: String, paidDate: String): String {
    return when (status) {
        1, 2 -> "Jatuh tempo pada ${DateHelper.formatDateToReadable(dueDate)}"
        else -> "Dilunasi pada ${DateHelper.formatDateToReadable(paidDate)}"
    }
}

@Composable
private fun getBillDateInformationColor(billStatus: Int): Color {
    return when (billStatus) {
        1, 3 -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> Red600
    }
}

data class BillListItemState(
    val id: Long,
    val title: String,
    val dueDate: String,
    val paidDate: String,
    val amount: Long,
    val isRecurring: Boolean,
    val status: Int,
    val nowPaidPeriod: Int,
)