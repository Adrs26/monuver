package com.android.monu.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.ui.theme.Blue800
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.Red600

@Composable
fun TransactionListItem(
    transactionState: TransactionListItemState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(
                icon = DatabaseCodeMapper.toChildCategoryIcon(transactionState.childCategory),
                backgroundColor = DatabaseCodeMapper
                    .toParentCategoryIconBackground(transactionState.parentCategory),
                modifier = Modifier.size(40.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = transactionState.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${transactionState.sourceName} · ${DateHelper.formatDateToReadable(transactionState.date)}",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }
            Text(
                text = NumberFormatHelper.formatToRupiah(transactionState.amount),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = transactionAmountColor(transactionState.type)
                )
            )
        }
    }
}

private fun transactionAmountColor(type: Int): Color {
    return when (type) {
        1001 -> Green600
        1002 -> Red600
        else -> Blue800
    }
}

data class TransactionListItemState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceName: String
)