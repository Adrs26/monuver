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
import com.android.monu.ui.theme.Red600

@Composable
fun BillListItem(
    billStatus: Int,
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
                text = "Langganan Spotify",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = getBillDateInformationText(billStatus),
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = getBillDateInformationColor(billStatus)
                )
            )
            Text(
                text = "Pembayaran ke-1",
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        Text(
            text = "Rp50.000",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

private fun getBillDateInformationText(billStatus: Int): String {
    return when (billStatus) {
        1, 2 -> "Jatuh tempo pada 25 Agustus 2025"
        else -> "Dilunasi pada 25 Agustus 2025"
    }
}

@Composable
private fun getBillDateInformationColor(billStatus: Int): Color {
    return when (billStatus) {
        1, 3 -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> Red600
    }
}