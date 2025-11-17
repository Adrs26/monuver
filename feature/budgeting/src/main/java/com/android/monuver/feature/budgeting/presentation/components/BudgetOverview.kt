package com.android.monuver.feature.budgeting.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.util.percentageOf
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.util.calculateProgressBarValue
import com.android.monuver.core.presentation.util.changeProgressBarColor
import com.android.monuver.feature.budgeting.R

@Composable
internal fun BudgetOverview(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.your_budgeting_this_period),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = totalMaxAmount.toRupiah(),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp)
            )
            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = totalUsedAmount.toRupiah(),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.already_used),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { calculateProgressBarValue(totalUsedAmount, totalMaxAmount) },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = changeProgressBarColor(totalUsedAmount, totalMaxAmount),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { null }
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        totalUsedAmount.percentageOf(totalMaxAmount)
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}