package com.android.monu.ui.feature.screen.billing.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.utils.DateHelper
import com.android.monu.utils.NumberHelper
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
                    color = getBillDateInformationColor(billState.status),
                    fontSize = 11.sp
                )
            )
            if (billState.isRecurring) {
                Text(
                    text = "Pembayaran ke-${billState.nowPaidPeriod}",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
        Text(
            text = NumberHelper.formatToRupiah(billState.amount),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
private fun getBillDateInformationText(status: Int, dueDate: String, paidDate: String): String {
    return when (status) {
        1, 2 -> stringResource(
            R.string.unpaid_bill_date_information,
            DateHelper.formatDateToReadable(dueDate)
        )
        else -> stringResource(
            R.string.paid_bill_date_information,
            DateHelper.formatDateToReadable(paidDate)
        )
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