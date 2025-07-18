package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.CategoryIcon
import com.android.monu.ui.theme.Blue800
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.MonuTheme
import com.android.monu.ui.theme.Orange600
import com.android.monu.ui.theme.Red600

@Composable
fun BudgetingListItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgetingListItemIcon(
                icon = R.drawable.ic_expense_utilities,
                backgroundColor = Blue800,
                amount = 5_000_000,
                maxAmount = 10_000_000
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Tagihan air",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "01/07/2025 - 31/07/2025",
                    modifier = Modifier.padding(top = 4.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Rp5.000.000",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp
                        )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(12.dp)
                            .padding(bottom = 2.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rp10.000.000",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun BudgetingListItemIcon(
    icon: Int,
    backgroundColor: Color,
    amount: Long,
    maxAmount: Long
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { amount.toFloat() / maxAmount.toFloat() },
            modifier = Modifier.size(48.dp),
            color = changeProgressBarColor(amount, maxAmount),
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp
        )
        CategoryIcon(
            icon = icon,
            backgroundColor = backgroundColor,
            modifier = Modifier.size(40.dp)
        )
    }
}

private fun changeProgressBarColor(amount: Long, maxAmount: Long): Color {
    return when {
        amount.toDouble() / maxAmount.toDouble() > 0.9 -> Red600
        amount.toDouble() / maxAmount.toDouble() > 0.6 -> Orange600
        else -> Green600
    }
}

@Composable
@Preview
fun BudgetingListItemPreview() {
    MonuTheme {
        BudgetingListItem()
    }
}