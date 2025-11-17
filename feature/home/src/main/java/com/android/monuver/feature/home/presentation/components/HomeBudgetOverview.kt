package com.android.monuver.feature.home.presentation.components

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
import com.android.monuver.core.domain.util.percentageOf
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.util.calculateProgressBarValue
import com.android.monuver.core.presentation.util.changeProgressBarColor
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.feature.home.R

@Composable
internal fun HomeBudgetOverview(
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
                    progress = { calculateProgressBarValue(totalUsedAmount, totalMaxAmount) },
                    modifier = Modifier.size(128.dp),
                    color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        totalUsedAmount.percentageOf(totalMaxAmount)
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
                    text = totalMaxAmount.toRupiah(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.used_amount),
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = totalUsedAmount.toRupiah(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.remained_amount),
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = if (totalUsedAmount >= totalMaxAmount) 0L.toRupiah() else
                        (totalMaxAmount - totalUsedAmount).toRupiah(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }
        }
    }
}