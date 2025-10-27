package com.android.monu.ui.feature.screen.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.feature.screen.budgeting.components.calculateProgressBar
import com.android.monu.ui.feature.screen.budgeting.components.changeProgressBarColor
import com.android.monu.utils.NumberHelper
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun HomeBudgetOverview(
    totalUsedAmount: Long,
    totalMaxAmount: Long,
    onNavigateToBudgeting: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.budgeting_recap),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.see_budgeting),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .debouncedClickable { onNavigateToBudgeting() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { calculateProgressBar(totalUsedAmount, totalMaxAmount) },
                    modifier = Modifier.size(128.dp),
                    color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        NumberHelper.formatToPercentageValue(totalUsedAmount, totalMaxAmount)
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                        fontSize = 20.sp
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 48.dp)
            ) {
                Text(
                    text = stringResource(R.string.maximum_amount),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = NumberHelper.formatToRupiah(totalMaxAmount),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.used_amount),
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = NumberHelper.formatToRupiah(totalUsedAmount),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.remained_amount),
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = if (totalUsedAmount >= totalMaxAmount) NumberHelper.formatToRupiah(0) else
                        NumberHelper.formatToRupiah(totalMaxAmount - totalUsedAmount),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }
        }
    }
}